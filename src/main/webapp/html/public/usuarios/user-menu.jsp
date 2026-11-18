<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.paula.checkmc.servlet.util.AttributeNames" %>
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
       <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=login">
    Autenticarse
</a>
      <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=register">
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

               
<c:if test="${sessionScope.tipoUsuario eq 'cliente'}">
    <a href="${pageContext.request.contextPath}/controller/private/cliente?action=detail">
        Ver perfil
    </a>
</c:if>


<c:if test="${sessionScope.tipoUsuario eq 'empleado'}">
    <a href="${pageContext.request.contextPath}/html/private/empleado/perfil.jsp">
        Ver perfil
    </a>
</c:if>

              

                <a class="cmc-salir"
                   href="${pageContext.request.contextPath}/controller/public/usuarios?action=logout">
                    Salir
                </a>
            </div>
        </details>
    </c:otherwise>
</c:choose>