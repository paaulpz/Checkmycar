package com.paula.checkmc.controller;

import java.io.File;
import java.io.IOException;

import com.paula.checkmc.model.ClienteDTO;
import com.paula.checkmc.servlet.util.AttributeNames;
import com.paula.checkmc.util.FileUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/controller/private/cliente")
public class PrivateClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public PrivateClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recuperar al usuario que ha iniciado sesion.
        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute(AttributeNames.USER) instanceof ClienteDTO)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Debes iniciar sesion como cliente");
            return;
        }

        ClienteDTO cliente = (ClienteDTO) session.getAttribute(AttributeNames.USER);
        String action = request.getParameter("action");

        if ("detail".equalsIgnoreCase(action)) {

            // Pendiente: mostrar los datos del cliente en una JSP.
            request.setAttribute("cliente", cliente);
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                    "Falta crear la pagina de detalle del cliente");

        } else if ("update".equalsIgnoreCase(action)) {

            // Pendiente: modificar los datos del cliente usando ClienteService.
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                    "La actualizacion del perfil esta pendiente");

        } else if ("get-image".equalsIgnoreCase(action)) {

            // La foto siempre pertenece al cliente de la sesion.
            // Es la misma carpeta que usa PublicUsuariosServlet.
            String carpeta = System.getProperty("user.home")
                    + "/Checkmycar/imagenes/perfiles/";
            String nombre = "cliente_" + cliente.getId();

            File foto = new File(carpeta + nombre + ".jpg");
            String tipo = "image/jpeg";

            if (!foto.exists()) {
                foto = new File(carpeta + nombre + ".png");
                tipo = "image/png";
            }

            if (!foto.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "El cliente no tiene foto");
                return;
            }

            // Enviar los bytes para que el navegador muestre la imagen.
            response.setContentType(tipo);
            response.setHeader("Cache-Control", "private, no-store");
            byte[] imagen = FileUtils.readFully(foto.getAbsolutePath());
            response.getOutputStream().write(imagen);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no valida");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
