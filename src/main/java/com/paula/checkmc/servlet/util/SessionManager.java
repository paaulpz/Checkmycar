package com.paula.checkmc.servlet.util;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionManager {

    private SessionManager() {
    }

    public static Object getAttribute(ServletRequest request, String name) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session == null) {
            return null;
        }

        return session.getAttribute(name);
    }

    public static void setAttribute(
            HttpServletRequest request,
            String name,
            Object value) {

        if (value == null) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute(name);
            }
        } else {
            request.getSession().setAttribute(name, value);
        }
    }
}