package com.paula.checkmc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
		} else if ("search".equalsIgnoreCase(action)) {
			// Action: Buscar presupuestos
			String nombre = request.getParameter("nombre");
			String precioFinal = request.getParameter("precioFinal");

			PresupuestoCriteria criteria = new PresupuestoCriteria();

			criteria.setCocheMatricula(null);

			try {

				Results<PresupuestoDTO> results =
				        presupuestoService.findByCriteria(
				                criteria, 1, Integer.MAX_VALUE);

				System.out.println("RESULTS = " + results);
				System.out.println("TOTAL = " + results.getTotal());
				System.out.println("PAGE = " + results.getPage());

				request.setAttribute("results", results);

				request.getRequestDispatcher(Views.PRESUPUESTO_SEARCH)
				        .forward(request, response);

			} catch (Exception e) {

			    e.printStackTrace();

			    PrintWriter writer = response.getWriter();
			    writer.println("<h1>Error buscando presupuestos</h1>");
			    writer.println("<p>" + e.getMessage() + "</p>");
			}

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

		doGet(request, response);
	}
}