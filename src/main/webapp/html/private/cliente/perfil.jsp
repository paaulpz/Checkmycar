<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi perfil | Checkmycar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body class="registro-pagina">
    <header class="barra-superior">
        <a href="${pageContext.request.contextPath}/html/public/index.jsp" class="marca">CHECK<span>MYCAR</span></a>
        <a class="volver" href="${pageContext.request.contextPath}/html/public/index.jsp">Volver al inicio</a>
    </header>

    <main class="perfil-contenedor">
        <section class="perfil-tarjeta">
            <div class="perfil-cabecera">
                <c:choose>
                    <c:when test="${tieneFoto}">
                        <img class="perfil-avatar" src="${pageContext.request.contextPath}/controller/private/cliente?action=get-image" alt="Mi foto de perfil">
                    </c:when>
                    <c:otherwise>
                        <div class="perfil-avatar perfil-avatar-vacio" aria-label="Sin foto de perfil"> <span>👤</span> </div>
                    </c:otherwise>
                </c:choose>
                <p class="etiqueta">ÁREA DE CLIENTES</p>
                <h1><c:out value="${cliente.nombre}"/> <c:out value="${cliente.primerApellido}"/></h1>
                <p>Mi perfil de Checkmycar</p>
            </div>

            <h2>Mis datos personales</h2>
            <div class="perfil-datos">
                <div><span>DNI/NIE</span><strong><c:out value="${cliente.dniNie}"/></strong></div>
                <div><span>Nombre</span><strong><c:out value="${cliente.nombre}"/></strong></div>
                <div><span>Primer apellido</span><strong><c:out value="${cliente.primerApellido}"/></strong></div>
                <div><span>Segundo apellido</span><strong><c:out value="${cliente.segundoApellido}"/></strong></div>
                <div><span>E-mail</span><strong><c:out value="${cliente.email}"/></strong></div>
                <div><span>Teléfono</span><strong><c:out value="${cliente.telefono}"/></strong></div>
                <div class="perfil-ancho"><span>Dirección</span><strong><c:out value="${cliente.direccion}"/></strong></div>
            </div>
            <a class="boton-registro boton-volver" href="${pageContext.request.contextPath}/html/public/index.jsp">Volver al inicio</a>
        </section>
    </main>
</body>
</html>
