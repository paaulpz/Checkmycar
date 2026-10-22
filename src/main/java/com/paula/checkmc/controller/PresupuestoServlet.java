package com.paula.checkmc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.paula.checkmc.model.PresupuestoCriteria;
import com.paula.checkmc.model.PresupuestoDTO;
import com.paula.checkmc.model.Results;
import com.paula.checkmc.service.PresupuestoService;
import com.paula.checkmc.service.impl.PresupuestoServiceImpl;
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

	public PresupuestoServlet() {
		presupuestoService = new PresupuestoServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter writer = response.getWriter();

		String action = request.getParameter("action");

		if ("detail".equalsIgnoreCase(action)) {

			String idStr = request.getParameter("id");

			try {
				PresupuestoDTO presupuesto =
						presupuestoService.findById(Long.valueOf(idStr));

				request.setAttribute("presupuesto", presupuesto);

				request.getRequestDispatcher(Views.PRESUPUESTO_DETAIL)
						.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}

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

			    writer.println("<h1>Error buscando presupuestos</h1>");
			    writer.println("<p>" + e.getMessage() + "</p>");
			}

		} else if ("delete".equalsIgnoreCase(action)) {

			String idStr = request.getParameter("id");

			try {

				presupuestoService.delete(Long.valueOf(idStr));

			} catch (Exception e) {

				e.printStackTrace();

				writer.append(
					"<i>No ha podido borrarse el presupuesto. "
					+ "Por favor contacte con su administrador</i>"
				);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}