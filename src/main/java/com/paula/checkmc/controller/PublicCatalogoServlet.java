
package com.paula.checkmc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paula.checkmc.model.ClienteCriteria;
import com.paula.checkmc.model.Coche;
import com.paula.checkmc.model.ContentData;
import com.paula.checkmc.model.ContentMetadata;
import com.paula.checkmc.model.Marca;
import com.paula.checkmc.model.Modelo;

import com.paula.checkmc.service.ClienteService;
import com.paula.checkmc.service.CocheService;
import com.paula.checkmc.service.ContentService;
import com.paula.checkmc.service.MarcaService;
import com.paula.checkmc.service.ModeloService;
import com.paula.checkmc.service.TipoCombustibleService;
import com.paula.checkmc.service.TipoMotorService;
import com.paula.checkmc.service.TipoTransmisionService;

import com.paula.checkmc.service.impl.ClienteServiceImpl;
import com.paula.checkmc.service.impl.CocheServiceImpl;
import com.paula.checkmc.service.impl.ContentServiceFileImpl;
import com.paula.checkmc.service.impl.MarcaServiceImpl;
import com.paula.checkmc.service.impl.ModeloServiceImpl;
import com.paula.checkmc.service.impl.TipoCombustibleServiceImpl;
import com.paula.checkmc.service.impl.TipoMotorServiceImpl;
import com.paula.checkmc.service.impl.TipoTransmisionServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/controller/public/catalogo")
@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 6L * 1024 * 1024)
public class PublicCatalogoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String JSP = "/html/private/coche/publicarCoche.jsp";

	private final ClienteService clienteService = new ClienteServiceImpl();

	private final CocheService cocheService = new CocheServiceImpl();

	private final MarcaService marcaService = new MarcaServiceImpl();

	private final ModeloService modeloService = new ModeloServiceImpl();

	private final TipoCombustibleService combustibleService = new TipoCombustibleServiceImpl();

	private final TipoMotorService motorService = new TipoMotorServiceImpl();

   private final TipoTransmisionService transmisionService = new TipoTransmisionServiceImpl(); 
   
   private final ContentService contentService = new ContentServiceFileImpl(); 
	
	

@Override
protected void doGet(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    mostrarFormulario(request, response);
}

private void mostrarFormulario(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    try {
        List<Marca> marcas = marcaService.findAll();
        Map<Long, List<Modelo>> modelosPorMarca = new HashMap<>();

        for (Marca marca : marcas) {
            modelosPorMarca.put(marca.getId(),
                    modeloService.findByMarca(marca.getId()));
        }

        request.setAttribute("marcas", marcas);
        request.setAttribute("modelosPorMarca", modelosPorMarca);

        request.setAttribute("clientes",
                clienteService.findByCriteria(
                        new ClienteCriteria(), 1, 1000).getPage());

        request.setAttribute("combustibles",
                combustibleService.findAll());

        request.setAttribute("motores",
                motorService.findAll());

        request.setAttribute("transmisiones",
                transmisionService.findAll());

        request.getRequestDispatcher(JSP).forward(request, response);

    } catch (Exception e) {
        throw new ServletException("Error cargando el formulario", e);
    }
    
}


@Override
protected void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    Long cocheId = null;

    try {
        Coche coche = new Coche();

        coche.setMatricula(request.getParameter("matricula"));
        coche.setNumeroBastidor(request.getParameter("bastidor"));
        coche.setDiagnostico(request.getParameter("diagnostico"));

        coche.setAno(Integer.valueOf(request.getParameter("ano")));
        coche.setPotenciaCv(Long.valueOf(request.getParameter("potencia")));
        coche.setKilometros(Long.valueOf(request.getParameter("kilometros")));

        coche.setClienteId(Long.valueOf(request.getParameter("clienteId")));
        coche.setMarcaId(Long.valueOf(request.getParameter("marcaId")));
        coche.setModeloId(Long.valueOf(request.getParameter("modeloId")));
        coche.setTipoCombustibleId(Long.valueOf(request.getParameter("combustibleId")));
        coche.setTipoMotorId(Long.valueOf(request.getParameter("motorId")));
        coche.setTipoTransmisionId(Long.valueOf(request.getParameter("transmisionId")));

        boolean modeloValido = modeloService
                .findByMarca(coche.getMarcaId())
                .stream()
                .anyMatch(m -> m.getId().equals(coche.getModeloId()));

        if (!modeloValido) {
            throw new IllegalArgumentException(
                    "El modelo no corresponde a la marca.");
        }

        Part foto = request.getPart("foto");

        if (foto == null || foto.getSize() == 0
                || foto.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Selecciona una imagen de hasta 5 MB.");
        }

        String formato;

        if ("image/jpeg".equals(foto.getContentType())) {
            formato = ".jpg";
        } else if ("image/png".equals(foto.getContentType())) {
            formato = ".png";
        } else {
            throw new IllegalArgumentException(
                    "Solo se permiten fotografías JPG y PNG.");
        }

        byte[] imagen;

        try (InputStream entrada = foto.getInputStream()) {
            imagen = entrada.readAllBytes();
        }

        if (javax.imageio.ImageIO.read(
                new java.io.ByteArrayInputStream(imagen)) == null) {
            throw new IllegalArgumentException(
                    "El archivo no es una imagen válida.");
        }

        cocheId = cocheService.create(coche);

        if (cocheId == null) {
            throw new IllegalStateException(
                    "No se ha obtenido el ID del coche.");
        }

        ContentMetadata metadata = new ContentMetadata();

        metadata.setEntityType("coche");
        metadata.setEntityId(String.valueOf(cocheId));
        metadata.setResourceId("principal");
        metadata.setFormat(formato);

        ContentData data = new ContentData();

        data.setMetadata(metadata);
        data.setRaw(imagen);

        contentService.save(data);

        response.sendRedirect(request.getContextPath()
                + "/controller/public/catalogo?guardado=" + cocheId);

    } catch (Exception e) {

        getServletContext().log("Error creando coche", e);

        request.setAttribute("errorCatalogo",
                cocheId == null
                ? "No se pudo registrar el coche: " + e.getMessage()
                : "Coche creado con ID " + cocheId
                  + ", pero no se pudo guardar la fotografía.");

        mostrarFormulario(request, response);
    }
}


}
