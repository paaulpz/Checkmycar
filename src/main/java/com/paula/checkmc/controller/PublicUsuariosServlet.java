
package com.paula.checkmc.controller;

import java.io.IOException;
import java.io.File;

import com.paula.checkmc.model.Cliente;
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
import com.paula.checkmc.util.FileUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/controller/public/usuarios")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class PublicUsuariosServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGIN_JSP =
            "/html/public/usuarios/login.jsp";

    private static final String REGISTER_JSP =
            "/html/public/usuarios/register.jsp";

    private final ClienteService clienteService =
            new ClienteServiceImpl();

    private final EmpleadoService empleadoService =
            new EmpleadoServiceImpl();

    private final RolService rolService =
            new RolServiceImpl();

    public PublicUsuariosServlet() {
        super();
    }

    // ACCIONES PUBLICAS: REGISTRO, LOGIN Y LOGOUT
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("register".equalsIgnoreCase(action)) {

            // Si entramos desde el navegador, mostramos el formulario.
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
                return;
            }

        
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setDniNie(request.getParameter("dni_nie"));
            nuevoCliente.setNombre(request.getParameter("nombre"));
            nuevoCliente.setPrimerApellido(request.getParameter("apellido1"));
            nuevoCliente.setSegundoApellido(request.getParameter("apellido2"));
            nuevoCliente.setEmail(request.getParameter("login"));
            nuevoCliente.setPassword(request.getParameter("password"));
            nuevoCliente.setTelefono(request.getParameter("telefono"));
            nuevoCliente.setDireccion(request.getParameter("direccion"));
            nuevoCliente.setLocalidadId( Long.valueOf(request.getParameter("localidadId")));
            nuevoCliente.setGeneroId(Long.valueOf(request.getParameter("generoId")));
            // Comprobar que los campos principales no estén vacíos.
            if (vacio(nuevoCliente.getDniNie()) || vacio(nuevoCliente.getNombre())
                    || vacio(nuevoCliente.getPrimerApellido())
                    || vacio(nuevoCliente.getEmail()) || vacio(nuevoCliente.getPassword())) {
                request.setAttribute("registerError", "Completa los campos obligatorios");
                request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
                return;
            }

            try {
               

                // Recuperar la fotografía
                Part foto = request.getPart("foto-perfil");
                String extension = "";

                if (foto != null && foto.getSize() > 0) {

                    if ("image/jpeg".equals(foto.getContentType())) {
                        extension = ".jpg";

                    } else if ("image/png".equals(foto.getContentType())) {
                        extension = ".png";

                    } else {
                        request.setAttribute("registerError",
                                "La foto debe ser JPG o PNG");
                        request.getRequestDispatcher(REGISTER_JSP)
                                .forward(request, response);
                        return;
                    }
                }

                // Registrar cliente en MySQL
                Long id = clienteService.register(nuevoCliente);

                if (id == null) {
                    request.setAttribute("registerError",
                            "No se pudo registrar el cliente");
                    request.getRequestDispatcher(REGISTER_JSP)
                            .forward(request, response);
                    return;
                }

                // Guardar la fotografía en el ordenador
                if (foto != null && foto.getSize() > 0) {

                    String carpeta = System.getProperty("user.home")
                            + "/Checkmycar/imagenes/perfiles/";

                    File directorio = new File(carpeta);

                    if (!directorio.exists() && !directorio.mkdirs()) {
                        throw new IOException("No se pudo crear la carpeta");
                    }

                    String ruta = carpeta + "cliente_" + id + extension;

                    try {
                        FileUtils.write(ruta,
                                foto.getInputStream().readAllBytes());

                    } catch (IOException e) {
                        getServletContext().log(
                                "Se registró el cliente, pero falló la foto", e);
                    }
                }

                // Volver al formulario de login.
                response.sendRedirect(request.getContextPath()
                        + "/controller/public/usuarios?action=login");

            } catch (Exception e) {
                getServletContext().log("Error registrando cliente", e);
                request.setAttribute("registerError", "Error al registrar el cliente");
                request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
            }

        } else if (action == null || "login".equalsIgnoreCase(action)) {

            if ("GET".equalsIgnoreCase(request.getMethod())) {
                mostrarFormulario(request, response);
            } else {
                iniciarSesion(request, response);
            }

        } else if ("logout".equalsIgnoreCase(action)) {

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/html/public/index.jsp");

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }

    // Igual que en el código del profesor: el POST usa doGet.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }

    // LOGIN ORIGINAL DE CHECKMYCAR: CLIENTES Y EMPLEADOS
    private void iniciarSesion(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {

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
