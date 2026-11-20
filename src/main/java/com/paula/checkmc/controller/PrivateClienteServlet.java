package com.paula.checkmc.controller;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    
    private static final Logger logger = LogManager.getLogger(PrivateClienteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute(AttributeNames.USER) instanceof ClienteDTO)) {
            response.sendRedirect(request.getContextPath() + "/controller/public/usuarios?action=login");
            return;
        }

        ClienteDTO cliente = (ClienteDTO) session.getAttribute(AttributeNames.USER);
        String action = request.getParameter("action");

        // Carpeta donde PublicUsuariosServlet guarda las fotos
        String carpeta = System.getProperty("user.home") + "/Checkmycar/imagenes/perfiles/";
        String nombre = "cliente_" + cliente.getId();
        File fotoJpg = new File(carpeta + nombre + ".jpg");
        File fotoPng = new File(carpeta + nombre + ".png");

        if ("detail".equalsIgnoreCase(action)) {

            request.setAttribute("cliente", cliente);
            request.setAttribute("tieneFoto", fotoJpg.isFile() || fotoPng.isFile());
            request.getRequestDispatcher("/html/private/cliente/perfil.jsp")
                   .forward(request, response);

        } else if ("get-image".equalsIgnoreCase(action)
                || "descargarFoto".equalsIgnoreCase(action)) {

            File foto;
            String tipo;

            if (fotoJpg.isFile()) {
                foto = fotoJpg;
                tipo = "image/jpeg";
            } else if (fotoPng.isFile()) {
                foto = fotoPng;
                tipo = "image/png";
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType(tipo);
            response.setHeader("Cache-Control", "private, no-store");

            if ("descargarFoto".equalsIgnoreCase(action)) {
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + foto.getName() + "\"");
            }
            try {
                byte[] imagen = FileUtils.readFully(foto.getAbsolutePath());
                response.getOutputStream().write(imagen);
            } catch (IOException e) {
                logger.error("No se pudo servir la foto de perfil", e);
                throw e;
            }

        } else if ("update".equalsIgnoreCase(action)) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                    "La modificación del perfil todavía no está implementada");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
        
           

    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
