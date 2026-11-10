
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>CheckMyCar</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/loginstyle.css">
</head>

<body>

    <main>

        <!-- LOGO -->
        <section id="logo">

            <img src="${pageContext.request.contextPath}/img/logo.png"
                 alt="Logo de CheckMyCar">

        </section>

        <!-- LOGIN -->
        <section id="login">

            <div class="contenido-login">

                <h2>Accede a tu cuenta</h2>

                <!-- MENSAJE DE ERROR -->
                <c:if test="${not empty loginError}">

                    <p style="color: red; text-align: center;">
                        <c:out value="${loginError}"/>
                    </p>

                </c:if>

                <form action="${pageContext.request.contextPath}/controller/public/usuarios"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="login">

                    <!-- DNI -->
                    <label for="dni">DNI</label>

                    <input type="text"
                           id="dni"
                           name="dni"
                           placeholder="Introduce tu DNI"
                           required>

                    <!-- CONTRASEÑA -->
                    <label for="password">Contraseña</label>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Introduce tu contraseña"
                           required>

                    <!-- TIPO DE USUARIO -->
                    <label for="tipoUsuario">
                        Tipo de usuario
                    </label>

                    <select id="tipoUsuario"
                            name="tipoUsuario"
                            required>

                        <option value="cliente"
                            ${tipoUsuarioSeleccionado ne 'empleado' ? 'selected' : ''}>
                            Cliente
                        </option>

                        <option value="empleado"
                            ${tipoUsuarioSeleccionado eq 'empleado' ? 'selected' : ''}>
                            Empleado
                        </option>

                    </select>

                    <!-- ROLES DESDE MYSQL -->
                    <div id="contenedorRol"
                         style="display: none;">

                        <label for="rol">
                            Tipo de empleado
                        </label>

                        <select id="rol"
                                name="rol"
                                disabled>

                            <option value="">
                                Selecciona un puesto
                            </option>

                            <c:forEach var="puesto" items="${roles}">

                                <option value="${puesto.id}"
                                    ${rolSeleccionado eq puesto.id ? 'selected' : ''}>

                                    <c:out value="${puesto.nombre}"/>

                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <!-- BOTÓN -->
                    <button type="submit">
                        Entrar
                    </button>

                </form>

               
<!-- REGISTRO -->
<a href="${pageContext.request.contextPath}/html/public/usuarios/register.jsp">
    ¿No tienes cuenta? Regístrate
</a>


                <!-- VOLVER -->
                <a href="${pageContext.request.contextPath}/html/public/index.jsp">
                    Volver al inicio
                </a>

            </div>

        </section>

    </main>

    <script>

        const tipoUsuario =
            document.getElementById("tipoUsuario");

        const contenedorRol =
            document.getElementById("contenedorRol");

        const rol =
            document.getElementById("rol");

        function actualizarTipoUsuario() {

            if (tipoUsuario.value === "empleado") {

                contenedorRol.style.display = "block";

                rol.disabled = false;
                rol.required = true;

            } else {

                contenedorRol.style.display = "none";

                rol.disabled = true;
                rol.required = false;

            }
        }

        tipoUsuario.addEventListener(
            "change",
            actualizarTipoUsuario
        );

        // COMPROBAR TIPO AL CARGAR EL LOGIN
        actualizarTipoUsuario();

    </script>

</body>
</html>
