package com.paula.checkmc.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/controller/private/cliente")
public class PrivateClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public PrivateClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("detail".equalsIgnoreCase(action)) {

            // Aquí mostraremos los datos del cliente

        } else if ("update".equalsIgnoreCase(action)) {

            // Aquí actualizaremos los datos del cliente

        } else {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Acción no válida"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}