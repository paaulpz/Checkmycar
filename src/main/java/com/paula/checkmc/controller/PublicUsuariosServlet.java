
package com.paula.checkmc.controller;

import java.io.IOException;

import com.paula.checkmc.model.ClienteDTO;
import com.paula.checkmc.model.EmpleadoDTO;
import com.paula.checkmc.model.Rol;

import com.paula.checkmc.service.ClienteService;
import com.paula.checkmc.service.EmpleadoService;
import com.paula.checkmc.service.RolService;

import com.paula.checkmc.service.impl.ClienteServiceImpl;
import com.paula.checkmc.service.impl.EmpleadoServiceImpl;
import com.paula.checkmc.service.impl.RolServiceImpl;

import com.paula.checkmc.servlet.util.AttributeNames;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/controller/public/usuarios")
public class PublicUsuariosServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGIN_JSP =
            "/html/public/usuarios/login.jsp";

    private final ClienteService clienteService =
            new ClienteServiceImpl();

    private final EmpleadoService empleadoService =
            new EmpleadoServiceImpl();

    private final RolService rolService =
            new RolServiceImpl();

    public PublicUsuariosServlet() {
        super();
    }

    // MOSTRAR LOGIN O CERRAR SESIÓN
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // CERRAR SESIÓN
        if ("logout".equalsIgnoreCase(action)) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/html/public/index.jsp"
            );
            return;
        }

        // MOSTRAR LOGIN CON LOS ROLES DE MYSQL
        if (action == null ||
                "login".equalsIgnoreCase(action)) {

            mostrarFormulario(request, response);
            return;
        }

        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Acción no válida"
        );
    }

    // INICIAR SESIÓN
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (!"login".equalsIgnoreCase(action)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Acción no válida"
            );
            return;
        }

        // DATOS DEL FORMULARIO
        String dni = request.getParameter("dni");
        String password = request.getParameter("password");
        String tipoUsuario = request.getParameter("tipoUsuario");
        String rolSeleccionado = request.getParameter("rol");

        if (dni == null || dni.isBlank()
                || password == null || password.isBlank()) {

            mostrarError(
                    request,
                    response,
                    "Introduce el DNI y la contraseña"
            );
            return;
        }

        try {

            Object usuario = null;
            Long rolId = null;

            // LOGIN CLIENTE
            if ("cliente".equals(tipoUsuario)) {

                ClienteDTO cliente = clienteService.login(
                        dni.trim(),
                        password
                );

                usuario = cliente;

                if (cliente != null) {
                    getServletContext().log(
                            "LOGIN CLIENTE CORRECTO"
                    );
                } else {
                    getServletContext().log(
                            "LOGIN CLIENTE INCORRECTO"
                    );
                }

            }

            // LOGIN EMPLEADO
            else if ("empleado".equals(tipoUsuario)) {

                // OBTENER ID REAL DEL ROL
                rolId = buscarRolId(rolSeleccionado);

                if (rolId == null) {

                    mostrarError(
                            request,
                            response,
                            "Selecciona un puesto válido"
                    );
                    return;
                }

                // AUTENTICAR CON MIDDLEWARE
                EmpleadoDTO empleado = empleadoService.login(
                        dni.trim(),
                        password,
                        rolId
                );

                usuario = empleado;

                if (empleado != null) {

                    getServletContext().log(
                            "LOGIN EMPLEADO CORRECTO"
                    );

                } else {

                    getServletContext().log(
                            "LOGIN EMPLEADO INCORRECTO"
                    );
                }

            } else {

                mostrarError(
                        request,
                        response,
                        "Selecciona Cliente o Empleado"
                );
                return;
            }

            // COMPROBAR AUTENTICACIÓN
            if (usuario == null) {

                mostrarError(
                        request,
                        response,
                        "DNI, contraseña o puesto incorrectos"
                );
                return;
            }

            // ELIMINAR SESIÓN ANTERIOR
            HttpSession anterior =
                    request.getSession(false);

            if (anterior != null) {
                anterior.invalidate();
            }

            // CREAR SESIÓN NUEVA
            HttpSession session =
                    request.getSession(true);

            session.setAttribute(
                    AttributeNames.USER,
                    usuario
            );

            session.setAttribute(
                    "tipoUsuario",
                    tipoUsuario
            );

            // GUARDAR ROL DEL EMPLEADO
            if ("empleado".equals(tipoUsuario)) {

                session.setAttribute(
                        "rolId",
                        rolId
                );
            }

            // REDIRIGIR AL INICIO
            response.sendRedirect(
                    request.getContextPath()
                    + "/html/public/index.jsp"
            );

        } catch (Exception e) {

            getServletContext().log(
                    "ERROR AL INICIAR SESIÓN",
                    e
            );

            mostrarError(
                    request,
                    response,
                    "Error al iniciar sesión. Revisa la consola de Tomcat."
            );
        }
    }

    // COMPROBAR ID DEL ROL EN MYSQL
    private Long buscarRolId(String valor)
            throws Exception {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        Long rolId;

        try {

            rolId = Long.valueOf(valor);

        } catch (NumberFormatException e) {
            return null;
        }

        for (Rol rol : rolService.findAll()) {

            if (rolId.equals(rol.getId())) {
                return rolId;
            }
        }

        return null;
    }

    // CARGAR ROLES Y MOSTRAR LOGIN
    private void mostrarFormulario(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            request.setAttribute(
                    "roles",
                    rolService.findAll()
            );

        } catch (Exception e) {

            getServletContext().log(
                    "ERROR AL CARGAR ROLES",
                    e
            );

            if (request.getAttribute("loginError") == null) {

                request.setAttribute(
                        "loginError",
                        "No se pudieron cargar los puestos de trabajo"
                );
            }
        }

        request.getRequestDispatcher(LOGIN_JSP)
                .forward(request, response);
    }

    // MOSTRAR ERROR
    private void mostrarError(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws ServletException, IOException {

        request.setAttribute(
                "loginError",
                mensaje
        );

        request.setAttribute(
                "tipoUsuarioSeleccionado",
                request.getParameter("tipoUsuario")
        );

        request.setAttribute(
                "rolSeleccionado",
                request.getParameter("rol")
        );

        // IMPORTANTE: VOLVER A CARGAR LOS ROLES
        mostrarFormulario(request, response);
    }
}
