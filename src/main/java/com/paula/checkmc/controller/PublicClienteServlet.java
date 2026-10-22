
package com.paula.checkmc.controller;

import java.io.IOException;

import com.paula.checkmc.model.ClienteDTO;
import com.paula.checkmc.service.ClienteService;
import com.paula.checkmc.service.impl.ClienteServiceImpl;
import com.paula.checkmc.servlet.util.AttributeNames;
import com.paula.checkmc.servlet.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/controller/public/cliente")
public class PublicClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ClienteService clienteService =
            new ClienteServiceImpl();

    public PublicClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("logout".equalsIgnoreCase(action)) {

            SessionManager.setAttribute(
                    request,
                    AttributeNames.USER,
                    null
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/html/public/index.jsp"
            );

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

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("login".equalsIgnoreCase(action)) {

            String dni = request.getParameter("dni");
            String password = request.getParameter("password");

            if (dni == null || dni.isBlank()
                    || password == null || password.isBlank()) {

                request.setAttribute(
                        "loginError",
                        "Debes introducir el DNI y la contraseña"
                );

                request.getRequestDispatcher(
                        "/html/public/cliente/login.jsp"
                ).forward(request, response);

                return;
            }

            try {

                ClienteDTO cliente =
                        clienteService.login(dni.trim(), password);

                if (cliente != null) {

                    SessionManager.setAttribute(
                            request,
                            AttributeNames.USER,
                            cliente
                    );

                    response.sendRedirect(
                            request.getContextPath()
                            + "/html/public/index.jsp"
                    );

                } else {

                    request.setAttribute(
                            "loginError",
                            "DNI o contraseña incorrectos"
                    );

                    request.getRequestDispatcher(
                            "/html/public/cliente/login.jsp"
                    ).forward(request, response);
                }

            } catch (Exception e) {

                getServletContext().log(
                        "Error al autenticar al cliente", e
                );

                request.setAttribute(
                        "loginError",
                        "No se pudo iniciar sesión. Comprueba tus datos."
                );

                request.getRequestDispatcher(
                        "/html/public/cliente/login.jsp"
                ).forward(request, response);
            }

        } else {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Acción no válida"
            );
        }
    }
}
