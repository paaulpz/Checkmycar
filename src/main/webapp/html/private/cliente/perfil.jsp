<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkmycar - Mi perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/perfil.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body class="perfil-body">

    <!-- Cabecera de Checkmycar -->
    <nav class="barra-superior">
        <a class="marca" href="${pageContext.request.contextPath}/html/public/index.jsp">
            CHECK<span>MYCAR</span>
        </a>
        <div class="barra-enlaces">
            <a href="${pageContext.request.contextPath}/html/public/index.jsp">Inicio</a>
            <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=logout">Cerrar sesión</a>
        </div>
    </nav>

    <main class="perfil-container">

        <!-- Cabecera del perfil -->
        <header class="perfil-header">
            <div class="perfil-header-izq">
                <!-- Avatar dinámico: imagen del cliente o inicial -->
                <c:choose>
                    <c:when test="${tieneFoto}">
                        <div class="avatar-img-container">
                            <img src="${pageContext.request.contextPath}/controller/private/cliente?action=get-image"
                                 alt="Foto de perfil" class="avatar-imagen">
                            <span class="status-dot"></span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="avatar-grande">
                            <c:out value="${fn:toUpperCase(fn:substring(cliente.nombre, 0, 1))}" />
                            <span class="status-dot"></span>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="perfil-header-textos">
                    <h1>Mi perfil</h1>
                    <p>Consulta tus datos personales de Checkmycar.</p>
                </div>
            </div>
            <div class="perfil-header-der">
                <span class="badge-cliente">Perfil de cliente</span>
            </div>
        </header>

        <!-- Sección: Datos personales -->
        <section class="perfil-seccion">
            <div class="seccion-header">
                <span class="seccion-categoria">INFORMACIÓN PERSONAL</span>
                <h2>Datos personales</h2>
                <p>Estos datos identifican tu cuenta de cliente.</p>
            </div>

            <div class="formulario-grid">
                <div class="input-group">
                    <label for="nombre">Nombre</label>
                    <input type="text" id="nombre" value="<c:out value='${cliente.nombre}' />" readonly>
                </div>
                <div class="input-group">
                    <label for="apellido1">Primer apellido</label>
                    <input type="text" id="apellido1" value="<c:out value='${cliente.primerApellido}' />" readonly>
                </div>
                <div class="input-group">
                    <label for="apellido2">Segundo apellido</label>
                    <input type="text" id="apellido2" value="<c:out value='${cliente.segundoApellido}' />" readonly>
                </div>
                <div class="input-group">
                    <label for="genero">Género</label>
                    <input type="text" id="genero" value="<c:out value='${cliente.nombreGenero}' />" readonly>
                </div>
                <div class="input-group">
                    <label for="dni">DNI / NIE</label>
                    <div class="input-readonly-container">
                        <input type="text" id="dni" value="<c:out value='${cliente.dniNie}' />" readonly>
                        <span class="material-icons icono-candado">lock</span>
                    </div>
                </div>
            </div>
            <p class="nota-informativa">
                <span class="material-icons icon-info">info_outline</span>
                Este perfil está en modo consulta. Los datos no se pueden modificar desde esta página.
            </p>
        </section>

        <!-- Sección: Contacto -->
        <section class="perfil-seccion">
            <div class="seccion-header">
                <span class="seccion-categoria">CONTACTO</span>
                <h2>Datos de contacto</h2>
                <p>Información de contacto y ubicación asociada a tu cuenta.</p>
            </div>

            <div class="formulario-grid">
                <div class="input-group">
                    <label for="telefono">Teléfono</label>
                    <input type="tel" id="telefono" value="<c:out value='${cliente.telefono}' />" readonly>
                </div>
                <div class="input-group">
                    <label for="localidad">Localidad</label>
                    <input type="text" id="localidad" value="<c:out value='${cliente.nombreLocalidad}' />" readonly>
                </div>
                <div class="input-group campo-completo">
                    <label for="direccion">Dirección</label>
                    <input type="text" id="direccion" value="<c:out value='${cliente.direccion}' />" readonly>
                </div>
            </div>
        </section>

        <!-- Sección: Cuenta -->
        <section class="perfil-seccion">
            <div class="seccion-header">
                <span class="seccion-categoria">ACCESO</span>
                <h2>Cuenta</h2>
                <p>Datos de acceso de tu cuenta Checkmycar.</p>
            </div>

            <div class="acceso-list">
                <div class="acceso-item">
                    <div class="acceso-info">
                        <label>Correo electrónico</label>
                        <div class="dato-protegido">
                            <span><c:out value="${cliente.email}" /></span>
                            <span class="material-icons icono-candado">lock</span>
                        </div>
                    </div>
                </div>

                <div class="acceso-item">
                    <div class="acceso-info">
                        <label>Contraseña</label>
                        <div class="dato-protegido"><span>••••••••</span></div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Barra de acciones: consulta -->
        <div class="acciones-flotantes">
            <span class="texto-cambios">Estás consultando tus datos personales.</span>
            <a class="btn-primario" href="${pageContext.request.contextPath}/html/public/index.jsp">
                Volver al inicio
            </a>
        </div>

    </main>
</body>
</html>
