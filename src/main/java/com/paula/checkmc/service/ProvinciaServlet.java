/*package com.paula.checkmc.service;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.gson.Gson;
import com.paula.checkmc.model.Provincia;
import com.paula.checkmc.service.impl.ProvinciaServiceImpl;

/**
 * Servlet implementation class ProvinciaServlet
 */
/*
@WebServlet("/provincia")
public class ProvinciaServlet extends HttpServlet {
	
	private ProvinciaService provinciaService = null; 
	private Gson gson = null; 

    public ProvinciaServlet() {
        super();
        
        provinciaService = new ProvinciaServiceImpl();
        gson = new Gson();
     
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
		String idPaisStr = request.getParameter("id-pais"); 
		Long idPais = Long.valueOf(idPaisStr); 
		
		List<Provincia> provincias = provinciaService.findByPais(idPais);
		
	response.setContentType("application/json");
	        String json = gson.toJson(provincias);
		
			
	OutputStream os = response.getOutputStream();
	os.write(json.getBytes());
	os.flush();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

} */
