<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.paula.checkmc.servlet.util.AttributeNames" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user.css">

<%
    pageContext.setAttribute(
        "usuarioConectado",
        session.getAttribute(AttributeNames.USER)
    );
%>

<c:choose>
    <c:when test="${empty usuarioConectado}">
        <a href="${pageContext.request.contextPath}/html/public/usuarios/login.jsp">
            Autenticarse
        </a>
        <a href="${pageContext.request.contextPath}/html/public/usuarios/register.jsp">
            Registrarse 
        </a>
    </c:when>

    <c:otherwise>
        <details class="cmc-cuenta">
            <summary>
                <c:out value="${usuarioConectado.nombre}" />
            </summary>

            <div class="cmc-panel">
                <h3>Tu cuenta</h3>

               
<a href="${pageContext.request.contextPath}/controller/private/cliente?action=detail">
    Ver perfil
</a>


                <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=changeAccount">
                    Cambiar cuenta
                </a>

                <a class="cmc-salir"
                   href="${pageContext.request.contextPath}/controller/public/usuarios?action=logout">
                    Salir
                </a>
            </div>
        </details>
    </c:otherwise>
</c:choose>