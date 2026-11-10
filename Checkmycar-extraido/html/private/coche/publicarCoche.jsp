
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/html/public/common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/catalogo.css">

<main class="catalogo">

    <div class="catalogo-titulo">
        <p class="catalogo-etiqueta">
            CHECKMYCAR · GESTIÓN DEL CATÁLOGO
        </p>

        <h1>Publicar vehículo</h1>

        <p>Registra los datos del coche y adjunta su fotografía principal.</p>
    </div>

    <!-- MENSAJES -->
    <c:if test="${not empty param.guardado}">
        <div class="catalogo-ok">
            Vehículo registrado con ID:
            <c:out value="${param.guardado}"/>
        </div>
    </c:if>

    <c:if test="${not empty errorCatalogo}">
        <div class="catalogo-error">
            <c:out value="${errorCatalogo}"/>
        </div>
    </c:if>

    <!-- FORMULARIO -->
    <form class="catalogo-form"
          method="post"
          enctype="multipart/form-data"
          action="${pageContext.request.contextPath}/controller/public/catalogo">

        <!-- IDENTIFICACIÓN -->
        <fieldset>

            <legend>Identificación del vehículo</legend>

            <div class="catalogo-grid">

                <label>
                    Matrícula *
                    <input type="text"
                           name="matricula"
                           maxlength="45"
                           placeholder="1234ABC"
                           required>
                </label>

                <label>
                    Número de bastidor *
                    <input type="text"
                           name="bastidor"
                           maxlength="45"
                           required>
                </label>

                <label>
                    Propietario *
                    <select name="clienteId" required>
                        <option value="">Seleccionar cliente</option>

                        <c:forEach var="cliente" items="${clientes}">
                            <option value="${cliente.id}">
                                <c:out value="${cliente.nombre}"/>
                                (ID: <c:out value="${cliente.id}"/>)
                            </option>
                        </c:forEach>
                    </select>
                </label>

                <label>
                    Marca *
                    <select name="marcaId" id="marcaId" required>
                        <option value="">Seleccionar marca</option>

                        <c:forEach var="marca" items="${marcas}">
                            <option value="${marca.id}">
                                <c:out value="${marca.nombre}"/>
                            </option>
                        </c:forEach>
                    </select>
                </label>

                <label>
                    Modelo *
                    <select name="modeloId" id="modeloId"
                            required disabled>

                        <option value="">
                            Selecciona primero una marca
                        </option>

                        <c:forEach var="marca" items="${marcas}">
                            <c:forEach var="modelo"
                                items="${modelosPorMarca[marca.id]}">

                                <option value="${modelo.id}"
                                        data-marca="${marca.id}">
                                    <c:out value="${modelo.nombre}"/>
                                </option>

                            </c:forEach>
                        </c:forEach>

                    </select>
                </label>

                <label>
                    Año *
                    <input type="number"
                           name="ano"
                           min="1901"
                           max="2100"
                           required>
                </label>

            </div>
        </fieldset>

        <!-- CARACTERÍSTICAS -->
        <fieldset>

            <legend>Características técnicas</legend>

            <div class="catalogo-grid">

                <label>
                    Combustible *
                    <select name="combustibleId" required>
                        <option value="">Seleccionar</option>

                        <c:forEach var="c" items="${combustibles}">
                            <option value="${c.id}">
                                <c:out value="${c.nombre}"/>
                            </option>
                        </c:forEach>
                    </select>
                </label>

                <label>
                    Tipo de motor *
                    <select name="motorId" required>
                        <option value="">Seleccionar</option>

                        <c:forEach var="m" items="${motores}">
                            <option value="${m.id}">
                                <c:out value="${m.nombre}"/>
                            </option>
                        </c:forEach>
                    </select>
                </label>

                <label>
                    Transmisión *
                    <select name="transmisionId" required>
                        <option value="">Seleccionar</option>

                        <c:forEach var="t" items="${transmisiones}">
                            <option value="${t.id}">
                                <c:out value="${t.nombre}"/>
                            </option>
                        </c:forEach>
                    </select>
                </label>

                <label>
                    Potencia (CV) *
                    <input type="number"
                           name="potencia"
                           min="0"
                           required>
                </label>

                <label>
                    Kilómetros *
                    <input type="number"
                           name="kilometros"
                           min="0"
                           required>
                </label>

                <label>
                    Estado / diagnóstico *
                    <input type="text"
                           name="diagnostico"
                           maxlength="45"
                           placeholder="Buen estado"
                           required>
                </label>

            </div>
        </fieldset>

        <!-- FOTOGRAFÍA -->
        <fieldset>

            <legend>Fotografía del vehículo</legend>

            <label class="catalogo-foto">
                Seleccionar fotografía principal *

                <input type="file"
                       id="foto"
                       name="foto"
                       accept="image/jpeg,image/png,image/webp"
                       required>
            </label>

            <img id="vistaPrevia"
                 class="catalogo-previa"
                 alt="Vista previa"
                 hidden>

        </fieldset>

        <!-- BOTONES -->
        <div class="catalogo-acciones">

            <button type="reset" class="catalogo-secundario">
                Limpiar
            </button>

            <button type="submit" class="catalogo-principal">
                Guardar vehículo y foto
            </button>

        </div>

    </form>

</main>

<script>

    // MODELOS SEGÚN LA MARCA
    const marca = document.getElementById("marcaId");
    const modelo = document.getElementById("modeloId");

    const opciones = Array.from(
        modelo.querySelectorAll("option[data-marca]")
    );

    function actualizarModelos() {

        const id = marca.value;

        modelo.value = "";
        modelo.disabled = !id;

        opciones.forEach(function(opcion) {

            opcion.hidden = opcion.dataset.marca !== id;
            opcion.disabled = opcion.dataset.marca !== id;

        });

        modelo.options[0].textContent = id
            ? "Seleccionar modelo"
            : "Selecciona primero una marca";
    }

    marca.addEventListener("change", actualizarModelos);

    actualizarModelos();

    // VISTA PREVIA DE LA FOTOGRAFÍA
    const foto = document.getElementById("foto");
    const vistaPrevia = document.getElementById("vistaPrevia");

    let ultimaUrl = null;

    foto.addEventListener("change", function() {

        if (ultimaUrl) {
            URL.revokeObjectURL(ultimaUrl);
            ultimaUrl = null;
        }

        if (!foto.files || foto.files.length === 0) {
            vistaPrevia.hidden = true;
            return;
        }

        ultimaUrl = URL.createObjectURL(foto.files[0]);

        vistaPrevia.src = ultimaUrl;
        vistaPrevia.hidden = false;
    });

    // LIMPIAR VISTA PREVIA
    foto.form.addEventListener("reset", function() {

        marca.value = "";
        actualizarModelos();

        vistaPrevia.hidden = true;

        if (ultimaUrl) {
            URL.revokeObjectURL(ultimaUrl);
            ultimaUrl = null;
        }

    });

</script>

<%@ include file="/html/public/common/footer.jsp" %>
