package com.paula.checkmc.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.gson.Gson;
import com.paula.checkmc.model.Localidad;
import com.paula.checkmc.service.impl.LocalidadServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/localidad")
public class LocalidadServlet extends HttpServlet {

    private LocalidadService localidadService = null;
    private Gson gson = null;

    public LocalidadServlet() {
        super();

        localidadService = new LocalidadServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idProvinciaStr = request.getParameter("id-provincia");
        Long idProvincia = Long.valueOf(idProvinciaStr);

        List<Localidad> localidades;
        try {
            localidades = localidadService.findByProvincia(idProvincia);
        } catch (Exception e) {
            throw new ServletException("No se pudieron cargar las localidades", e);
        }

        response.setContentType("application/json");
        String json = gson.toJson(localidades);

        OutputStream os = response.getOutputStream();
        os.write(json.getBytes());
        os.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }
}