package com.paula.checkmc.filter;

import java.io.IOException;

import com.paula.checkmc.model.ClienteDTO;
import com.paula.checkmc.model.EmpleadoDTO;
import com.paula.checkmc.servlet.util.AttributeNames;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({
    "/html/private/*",
    "/controller/private/*",
    "/presupuesto"
})
public class AuthenticatorFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        Object usuario = session == null
                ? null
                : session.getAttribute(AttributeNames.USER);

        if (usuario == null) {
            response.sendRedirect(request.getContextPath()
                    + "/controller/public/usuarios?action=login");
            return;
        }

        String ruta = request.getServletPath();

        boolean esRutaEmpleado = "/presupuesto".equals(ruta)
                || ruta.startsWith("/html/private/presupuesto/")
                || "/html/private/empleado/perfil.jsp".equals(ruta);

        boolean tienePermiso = esRutaEmpleado
                ? usuario instanceof EmpleadoDTO
                : usuario instanceof ClienteDTO;

        if (!tienePermiso) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}