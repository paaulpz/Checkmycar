
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro | Checkmycar</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/register.css">
</head>

<body class="registro-pagina">

    <header class="barra-superior">
        <a href="${pageContext.request.contextPath}/html/public/index.jsp"
           class="marca">
            CHECK<span>MYCAR</span>
        </a>

        <a class="volver"
           href="${pageContext.request.contextPath}/controller/public/usuarios?action=login">
            Iniciar sesión
        </a>
    </header>

    <main class="registro-contenedor">
        <section class="registro-tarjeta">

            <div class="registro-encabezado">
                <p class="etiqueta">ÁREA DE CLIENTES</p>
                <h1>Crea tu cuenta</h1>
                <p>Regístrate para gestionar tus vehículos y tus presupuestos.</p>
            </div>

            <c:if test="${not empty registerError}">
                <p class="mensaje-error">
                    <c:out value="${registerError}" />
                </p>
            </c:if>

            <form action="${pageContext.request.contextPath}/controller/public/usuarios"
                  method="post"
                  enctype="multipart/form-data"
                  class="registro-formulario">

                <input type="hidden" name="action" value="register">

                <div class="registro-campos">

                    <!-- CORREO -->
                    <div class="campo campo-completo">
                        <label for="login">
                            Correo electrónico <span>*</span>
                        </label>

                        <input type="email"
                               id="login"
                               name="login"
                               placeholder="correo@ejemplo.com"
                               autocomplete="email"
                               maxlength="256"
                               required>
                    </div>

                    <!-- CONTRASEÑA -->
                    <div class="campo campo-completo">
                        <label for="password">
                            Contraseña <span>*</span>
                        </label>

                        <input type="password"
                               id="password"
                               name="password"
                               autocomplete="new-password"
                               minlength="8"
                               required>
                    </div>

                    <!-- DNI -->
                    <div class="campo campo-completo">
                        <label for="dni_nie">
                            DNI o NIE <span>*</span>
                        </label>

                        <input type="text"
                               id="dni_nie"
                               name="dni_nie"
                               placeholder="12345678Z"
                               maxlength="20"
                               required>
                    </div>

                    <!-- NOMBRE -->
                    <div class="campo">
                        <label for="nombre">
                            Nombre <span>*</span>
                        </label>

                        <input type="text"
                               id="nombre"
                               name="nombre"
                               autocomplete="given-name"
                               maxlength="45"
                               required>
                    </div>

                    <!-- PRIMER APELLIDO -->
                    <div class="campo">
                        <label for="apellido1">
                            Primer apellido <span>*</span>
                        </label>

                        <input type="text"
                               id="apellido1"
                               name="apellido1"
                               maxlength="45"
                               required>
                    </div>

                    <!-- SEGUNDO APELLIDO -->
                    <div class="campo">
                        <label for="apellido2">
                            Segundo apellido
                        </label>

                        <input type="text"
                               id="apellido2"
                               name="apellido2"
                               maxlength="45">
                    </div>

                    <!-- TELEFONO -->
                    <div class="campo">
                        <label for="telefono">
                            Teléfono <span>*</span>
                        </label>

                        <input type="tel"
                               id="telefono"
                               name="telefono"
                               autocomplete="tel"
                               maxlength="45"
                               required>
                    </div>

                    <!-- PAIS -->
                    <div class="campo campo-completo">
                        <label for="paisId">
                            País <span>*</span>
                        </label>

                        <select id="paisId" name="paisId" required>
                            <option value="">Selecciona un país</option>

                            <c:forEach var="pais" items="${paises}">
                                <option value="${pais.key}">
                                    <c:out value="${pais.value}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- PROVINCIA -->
                    <div class="campo">
                        <label for="provinciaId">
                            Provincia <span>*</span>
                        </label>

                        <select id="provinciaId"
                                name="provinciaId"
                                required
                                disabled>

                            <option value="">
                                Primero selecciona un país
                            </option>

                            <c:forEach var="provincia" items="${provincias}">
                                <option value="${provincia.id}"
                                        data-pais="${provincia.paisId}">
                                    <c:out value="${provincia.nombre}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- LOCALIDAD -->
                    <div class="campo">
                        <label for="localidadId">
                            Localidad <span>*</span>
                        </label>

                        <select id="localidadId"
                                name="localidadId"
                                required
                                disabled>

                            <option value="">
                                Primero selecciona una provincia
                            </option>

                            <c:forEach var="localidad" items="${localidades}">
                                <option value="${localidad.id}"
                                        data-provincia="${localidad.provinciaId}">
                                    <c:out value="${localidad.nombre}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    


                    <!-- GENERO -->
                    <div class="campo campo-completo">
                        <label for="generoId">
                            Género <span>*</span>
                        </label>

                        <select id="generoId" name="generoId" required>
                            <option value="">Selecciona una opción</option>

                            <c:forEach var="genero" items="${generos}">
                                <option value="${genero.key}">
                                    <c:out value="${genero.value}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- DIRECCION -->
                    <div class="campo campo-completo">
                        <label for="direccion">
                            Dirección
                        </label>

                        <input type="text"
                               id="direccion"
                               name="direccion"
                               autocomplete="street-address"
                               maxlength="45">
                    </div>

                </div>

                <!-- FOTO DE PERFIL -->
                <div class="foto-apartado">

                    <div class="foto-circulo" id="vista-previa">
                        <span>+</span>
                    </div>

                    <div class="foto-textos">
                        <label for="foto">
                            Foto de perfil (opcional)
                        </label>

                        <input type="file"
                               id="foto"
                               name="foto-perfil"
                               accept="image/jpeg,image/png">

                        <small>JPG o PNG · Máximo 5 MB</small>
                    </div>

                </div>

                <!-- BOTON REGISTRO -->
                <button type="submit" class="boton-registro">
                    Crear cuenta
                </button>

                <p class="registro-pie">
                    ¿Ya tienes cuenta?

                    <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=login">
                        Inicia sesión
                    </a>
                </p>

            </form>

        </section>
    </main>

    <script src="${pageContext.request.contextPath}/js/register.js"></script>

</body>
</html>
