
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

<!-- FORMULARIO CREACIÓN DE COCHES -->

<form action="${pageContext.request.contextPath}/controller/public/catalogo"
      method="post"
      enctype="multipart/form-data"
      class="catalogo-form">

    <label>Matrícula *</label>
    <input type="text" name="matricula" required maxlength="45">

    <label>Número de bastidor *</label>
    <input type="text" name="bastidor" required maxlength="45">

    <label>Año *</label>
    <input type="number" name="ano" min="1901" max="2155" required>

    <label>Potencia (CV) *</label>
    <input type="number" name="potencia" min="0" required>

    <label>Kilómetros *</label>
    <input type="number" name="kilometros" min="0" required>

    <label>Diagnóstico *</label>
    <textarea name="diagnostico" maxlength="45" required></textarea>

    <!-- CLIENTES DE LA BASE DE DATOS -->

    <label>Cliente *</label>
    <select name="clienteId" required>
        <option value="">Seleccionar cliente</option>

        <c:forEach var="cliente" items="${clientes}">
            <option value="${cliente.id}">
                <c:out value="${cliente.nombre}"/>
            </option>
        </c:forEach>
    </select>

    <!-- MARCAS DE LA BASE DE DATOS -->

    <label>Marca *</label>
    <select name="marcaId" id="marcaId" required>
        <option value="">Seleccionar marca</option>

        <c:forEach var="marca" items="${marcas}">
            <option value="${marca.id}">
                <c:out value="${marca.nombre}"/>
            </option>
        </c:forEach>
    </select>

    <!-- MODELOS SEGÚN LA MARCA -->

    <label>Modelo *</label>
    <select name="modeloId" id="modeloId" required disabled>
        <option value="">Seleccionar modelo</option>
    </select>

    <c:forEach var="grupo" items="${modelosPorMarca}">
        <template id="modelos-${grupo.key}">
            <c:forEach var="modelo" items="${grupo.value}">
                <option value="${modelo.id}">
                    <c:out value="${modelo.nombre}"/>
                </option>
            </c:forEach>
        </template>
    </c:forEach>

    <!-- COMBUSTIBLE -->

    <label>Combustible *</label>
    <select name="combustibleId" required>
        <option value="">Seleccionar combustible</option>

        <c:forEach var="combustible" items="${combustibles}">
            <option value="${combustible.id}">
                <c:out value="${combustible.nombre}"/>
            </option>
        </c:forEach>
    </select>

    <!-- MOTOR -->

    <label>Tipo de motor *</label>
    <select name="motorId" required>
        <option value="">Seleccionar motor</option>

        <c:forEach var="motor" items="${motores}">
            <option value="${motor.id}">
                <c:out value="${motor.nombre}"/>
            </option>
        </c:forEach>
    </select>

    <!-- TRANSMISIÓN -->

    <label>Transmisión *</label>
    <select name="transmisionId" required>
        <option value="">Seleccionar transmisión</option>

        <c:forEach var="transmision" items="${transmisiones}">
            <option value="${transmision.id}">
                <c:out value="${transmision.nombre}"/>
            </option>
        </c:forEach>
    </select>

    <!-- FOTOGRAFÍA -->

    <label>Fotografía del vehículo *</label>
    <input type="file" name="foto" id="foto"
           accept="image/jpeg,image/png" required>

    <img id="fotoPreview" alt="Vista previa"
         width="180" hidden>

    <button type="submit">Publicar vehículo</button>

</form>

<script>
const marca = document.getElementById("marcaId");
const modelo = document.getElementById("modeloId");

marca.addEventListener("change", () => {
    modelo.replaceChildren(new Option("Seleccionar modelo", ""));
    const plantilla = document.getElementById("modelos-" + marca.value);
    if (plantilla) modelo.append(plantilla.content.cloneNode(true));
    modelo.disabled = !plantilla;
});

document.getElementById("foto").addEventListener("change", e => {
    const archivo = e.target.files[0];
    const preview = document.getElementById("fotoPreview");
    preview.hidden = !archivo;
    if (archivo) {
        const url = URL.createObjectURL(archivo);
        preview.onload = () => URL.revokeObjectURL(url);
        preview.src = url;
    } else {
        preview.removeAttribute("src");
    }
});
</script>

</main>



<%@ include file="/html/public/common/footer.jsp" %>
