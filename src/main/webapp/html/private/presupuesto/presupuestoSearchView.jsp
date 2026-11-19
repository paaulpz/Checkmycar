<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ include file="/html/public/common/header.jsp" %>

<c:url var="urlCssPresupuestos" value="/css/presupuestos.css">
    <c:param name="v" value="3" />
</c:url>
<link rel="stylesheet" href="<c:out value='${urlCssPresupuestos}' />">

<c:url var="urlBuscadorPresupuestos" value="/presupuesto">
    <c:param name="action" value="search" />
</c:url>

<form class="presupuesto-busqueda" action="<c:url value='/presupuesto' />"
      method="get" role="search" aria-labelledby="titulo-busqueda">
    <input type="hidden" name="action" value="search">
    <input type="hidden" name="buscar" value="1">

    <h1 id="titulo-busqueda">Buscar presupuestos</h1>

    <div class="presupuesto-filtros">
        <div class="presupuesto-campo">
            <label for="presupuesto-numero">N.º de presupuesto</label>
            <input id="presupuesto-numero" name="numero" type="text" inputmode="numeric"
                   pattern="[0-9]+" placeholder="Ej. 12" value="<c:out value='${param.numero}' />">
        </div>

        <div class="presupuesto-campo">
            <label for="presupuesto-nombre">Nombre</label>
            <input id="presupuesto-nombre" name="nombre" type="search"
                   placeholder="Ej. Cambio de aceite" value="<c:out value='${param.nombre}' />">
        </div>

        <div class="presupuesto-campo">
            <label for="presupuesto-estado">Estado</label>
            <select id="presupuesto-estado" name="estado">
                <option value="">Todos los estados</option>
                <c:forEach var="estado" items="${estadosBusqueda}">
                    <option value="<c:out value='${estado.id}' />"${param.estado eq estado.id.toString() ? ' selected' : ''}><c:out value="${estado.nombre}" /></option>
                </c:forEach>
            </select>
        </div>
    </div>

    <c:if test="${not empty errorBusqueda}">
        <p class="presupuesto-error" role="alert"><c:out value="${errorBusqueda}" /></p>
    </c:if>

    <div class="presupuesto-acciones">
        <button type="submit">Buscar</button>
        <a href="<c:out value='${urlBuscadorPresupuestos}' />">Limpiar filtros</a>
    </div>
</form>

<c:if test="${busquedaRealizada}">
    <section class="presupuesto-listado" aria-labelledby="titulo-resultados">
        <h2 id="titulo-resultados">Resultados</h2>

        <c:choose>
            <c:when test="${empty presupuestosEncontrados}">
                <p class="presupuesto-sin-resultados">No hay presupuestos que coincidan con los filtros.</p>
            </c:when>
            <c:otherwise>
                <p class="presupuesto-recuento">
                    <c:out value="${totalEncontrados}" />
                    ${totalEncontrados eq 1 ? 'presupuesto encontrado' : 'presupuestos encontrados'}
                </p>
                <ul class="presupuesto-tarjetas" role="list">
                    <c:forEach var="presupuesto" items="${presupuestosEncontrados}">
                        <c:url var="urlDetallePresupuesto" value="/presupuesto">
                            <c:param name="action" value="detail" />
                            <c:param name="id" value="${presupuesto.id}" />
                            <c:param name="buscar" value="1" />
                            <c:param name="numero" value="${param.numero}" />
                            <c:param name="nombre" value="${param.nombre}" />
                            <c:param name="estado" value="${param.estado}" />
                        </c:url>
                        <li>
                            <a class="presupuesto-tarjeta" href="<c:out value='${urlDetallePresupuesto}' />">
                                <span class="presupuesto-referencia">N.º <c:out value="${presupuesto.id}" /></span>
                                <strong class="presupuesto-nombre"><c:out value="${presupuesto.nombre}" default="Presupuesto" /></strong>
                                <span class="presupuesto-datos">
                                    <span><c:out value="${estadosPorId[presupuesto.estadoPresupuestoId]}" default="Sin estado" /></span>
                                    <span>Fecha <c:out value="${presupuesto.fecha}" default="—" /></span>
                                    <span>
                                        Precio final
                                        <c:choose>
                                            <c:when test="${empty presupuesto.precioFinal}">—</c:when>
                                            <c:otherwise><fmt:formatNumber value="${presupuesto.precioFinal}" minFractionDigits="2" maxFractionDigits="2" /> €</c:otherwise>
                                        </c:choose>
                                    </span>
                                </span>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </section>
</c:if>

<%@ include file="/html/public/common/footer.jsp" %>
