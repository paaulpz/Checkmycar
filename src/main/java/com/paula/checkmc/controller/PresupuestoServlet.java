package com.paula.checkmc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.paula.checkmc.model.Cliente;
import com.paula.checkmc.model.Empleado;
import com.paula.checkmc.model.EmpleadoDTO;
import com.paula.checkmc.model.EstadoPresupuesto;
import com.paula.checkmc.model.LineaPresupuestoDTO;
import com.paula.checkmc.model.PiezaDTO;
import com.paula.checkmc.model.PresupuestoCriteria;
import com.paula.checkmc.model.PresupuestoDTO;
import com.paula.checkmc.model.Results;
import com.paula.checkmc.service.ClienteService;
import com.paula.checkmc.service.EmpleadoService;
import com.paula.checkmc.service.EstadoPresupuestoService;
import com.paula.checkmc.service.LineaPresupuestoService;
import com.paula.checkmc.service.PiezaService;
import com.paula.checkmc.service.PresupuestoEmpleadoService;
import com.paula.checkmc.service.PresupuestoService;
import com.paula.checkmc.service.impl.ClienteServiceImpl;
import com.paula.checkmc.service.impl.EmpleadoServiceImpl;
import com.paula.checkmc.service.impl.EstadoPresupuestoServiceImpl;
import com.paula.checkmc.service.impl.LineaPresupuestoServiceImpl;
import com.paula.checkmc.service.impl.PiezaServiceImpl;
import com.paula.checkmc.service.impl.PresupuestoEmpleadoServiceImpl;
import com.paula.checkmc.service.impl.PresupuestoServiceImpl;
import com.paula.checkmc.servlet.util.AttributeNames;
import com.paula.checkmc.servlet.util.SessionManager;
import com.paula.checkmc.view.Views;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PresupuestoServlet
 */
@WebServlet("/presupuesto")
public class PresupuestoServlet extends HttpServlet {

	private PresupuestoService presupuestoService = null;
	private final ClienteService clienteService = new ClienteServiceImpl();
	private final EstadoPresupuestoService estadoService = new EstadoPresupuestoServiceImpl();
	private final LineaPresupuestoService lineaService = new LineaPresupuestoServiceImpl();
	private final PiezaService piezaService = new PiezaServiceImpl();
	private final PresupuestoEmpleadoService asignacionService = new PresupuestoEmpleadoServiceImpl();
	private final EmpleadoService empleadoService = new EmpleadoServiceImpl();

	public PresupuestoServlet() {
		presupuestoService = new PresupuestoServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Object usuario = SessionManager.getAttribute(request, AttributeNames.USER);
		if (usuario == null) {
			response.sendRedirect(request.getContextPath()
					+ "/controller/public/usuarios?action=login");
			return;
		}
		if (!(usuario instanceof EmpleadoDTO)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		response.setHeader("Cache-Control", "no-store");
		String action = request.getParameter("action");

		if ("detail".equalsIgnoreCase(action)) {
			mostrarDetalle(request, response);
		} else if (action == null || "search".equalsIgnoreCase(action)) {
			buscarPresupuestos(request, response);
		} else if ("delete".equalsIgnoreCase(action)) {

			String idStr = request.getParameter("id");

			try {

				presupuestoService.delete(Long.valueOf(idStr));

			} catch (Exception e) {

				e.printStackTrace();

				response.getWriter().append(
					"<i>No ha podido borrarse el presupuesto. "
					+ "Por favor contacte con su administrador</i>"
				);
			}
		}
	}

	private void buscarPresupuestos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nombre = leerFiltro(request, "nombre").toLowerCase(Locale.ROOT);
		String numero = leerFiltro(request, "numero");
		String estado = leerFiltro(request, "estado");
		boolean buscar = "1".equals(request.getParameter("buscar"))
				|| request.getParameter("nombre") != null || !numero.isEmpty() || !estado.isEmpty()
				|| "POST".equals(request.getMethod());

		try {
			List<EstadoPresupuesto> estados = estadoService.findAll();
			request.setAttribute("estadosBusqueda", estados);

			if (buscar) {
				Long id = leerFiltroNumerico(numero);
				Long estadoId = leerFiltroNumerico(estado);
				List<PresupuestoDTO> encontrados = new ArrayList<>();

				if (id != null) {
					PresupuestoDTO presupuesto = presupuestoService.findById(id);
					if (presupuesto != null
							&& (estadoId == null || estadoId.equals(presupuesto.getEstadoPresupuestoId()))
							&& coincideNombre(presupuesto, nombre)) {
						encontrados.add(presupuesto);
					}
				} else {
					PresupuestoCriteria criteria = new PresupuestoCriteria();
					criteria.setEstadoPresupuestoId(estadoId);
					criteria.setAscDesc(false);

					Results<PresupuestoDTO> resultados =
							presupuestoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
					for (PresupuestoDTO presupuesto : resultados.getPage()) {
						if (coincideNombre(presupuesto, nombre)) {
							encontrados.add(presupuesto);
						}
					}
				}

				Map<Long, String> estadosPorId = new HashMap<>();
				for (EstadoPresupuesto opcion : estados) {
					estadosPorId.put(opcion.getId(), opcion.getNombre());
				}
				request.setAttribute("estadosPorId", estadosPorId);
				request.setAttribute("presupuestosEncontrados", encontrados);
				request.setAttribute("totalEncontrados", encontrados.size());
				request.setAttribute("busquedaRealizada", true);
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.setAttribute("errorBusqueda", "Introduce un número de presupuesto y un estado válidos.");
		} catch (Exception e) {
			throw new ServletException("Error buscando presupuestos", e);
		}

		request.getRequestDispatcher(Views.PRESUPUESTO_SEARCH).forward(request, response);
	}

	private String leerFiltro(HttpServletRequest request, String nombre) {
		String valor = request.getParameter(nombre);
		return valor == null ? "" : valor.trim();
	}

	private Long leerFiltroNumerico(String filtro) {
		if (filtro.isEmpty()) {
			return null;
		}
		long valor = Long.parseLong(filtro);
		if (valor <= 0) {
			throw new NumberFormatException();
		}
		return valor;
	}

	private boolean coincideNombre(PresupuestoDTO presupuesto, String nombreBuscado) {
		String nombre = presupuesto.getNombre();
		return nombreBuscado.isEmpty()
				|| (nombre != null && nombre.toLowerCase(Locale.ROOT).contains(nombreBuscado));
	}

	private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (id <= 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			PresupuestoDTO presupuesto = presupuestoService.findById(id);
			if (presupuesto == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			Cliente cliente = clienteService.findById(presupuesto.getClienteId());
			EstadoPresupuesto estado = estadoService.findById(presupuesto.getEstadoPresupuestoId());
			List<LineaPresupuestoDTO> lineas = lineaService.findByPresupuesto(id);

			for (LineaPresupuestoDTO linea : lineas) {
				PiezaDTO pieza = piezaService.findById(linea.getPiezaId());
				if (pieza != null) {
					linea.setNombrePieza(pieza.getNombre());
				}
			}

			List<Empleado> empleados = new ArrayList<>();
			for (Long empleadoId : asignacionService.findEmpleadosByPresupuesto(id)) {
				Empleado empleado = empleadoService.findById(empleadoId);
				if (empleado != null) {
					empleados.add(empleado);
				}
			}

			request.setAttribute("empleadosPresupuesto", empleados);
			request.setAttribute("presupuesto", presupuesto);
			request.setAttribute("clientePresupuesto", cliente);
			request.setAttribute("estadoPresupuesto", estado);
			request.setAttribute("lineasPresupuesto", lineas);
		} catch (Exception e) {
			throw new ServletException("Error cargando el detalle del presupuesto", e);
		}

		request.getRequestDispatcher(Views.PRESUPUESTO_DETAIL).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		doGet(request, response);
	}
}
