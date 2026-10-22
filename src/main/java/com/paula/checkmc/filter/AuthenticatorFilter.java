package com.paula.checkmc.filter;

import java.io.IOException;

import com.paula.checkmc.model.ClienteDTO;
import com.paula.checkmc.servlet.util.AttributeNames;
import com.paula.checkmc.servlet.util.SessionManager;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({
    "/html/private/*",
    "/controller/private/*"
})
public class AuthenticatorFilter extends HttpFilter implements Filter {

    public AuthenticatorFilter() {
        super();
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        ClienteDTO cliente = (ClienteDTO) SessionManager.getAttribute(
                request,
                AttributeNames.USER
        );

        if (cliente != null) {

            chain.doFilter(request, response);

        } else {

        	    HttpServletRequest httpRequest =
        	            (HttpServletRequest) request;

        	    HttpServletResponse httpResponse =
        	            (HttpServletResponse) response;

        	    httpResponse.sendRedirect(
        	            httpRequest.getContextPath()
        	            + "/html/public/cliente/login.jsp"
        	    );
        	}
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}