<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="/html/public/common/header.jsp" %>

<link rel="stylesheet" href="<c:url value='/css/presupuestos.css' />">

<section class="presupuesto-resultados">
    <c:choose>
        <c:when test="${empty presupuesto}">
            <h1>Presupuesto no encontrado</h1>
        </c:when>
        <c:otherwise>
            <h1><c:out value="${presupuesto.nombre}" /></h1>
            <p>Número: <c:out value="${presupuesto.id}" /></p>
            <p>Fecha: <c:out value="${presupuesto.fecha}" default="—" /></p>
            <p>Cliente:
                <c:choose>
                    <c:when test="${not empty clientePresupuesto}">
                        <c:out value="${clientePresupuesto.nombre} ${clientePresupuesto.primerApellido} ${clientePresupuesto.segundoApellido}" />
                    </c:when>
                    <c:otherwise>#<c:out value="${presupuesto.clienteId}" /></c:otherwise>
                </c:choose>
            </p>
            <p>Estado: <c:out value="${empty estadoPresupuesto ? presupuesto.estadoPresupuestoId : estadoPresupuesto.nombre}" default="—" /></p>
            <p>Precio final: <c:out value="${presupuesto.precioFinal}" default="—" /></p>

            <h2>Empleados asignados</h2>
            <c:choose>
                <c:when test="${empty empleadosPresupuesto}">
                    <p>Sin empleados asignados.</p>
                </c:when>
                <c:otherwise>
                    <ul>
                        <c:forEach var="asignado" items="${empleadosPresupuesto}">
                            <li><c:out value="${asignado.nombre} ${asignado.primerApellido} ${asignado.segundoApellido}" /></li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>

            <h2>Líneas del presupuesto</h2>
            <c:choose>
                <c:when test="${empty lineasPresupuesto}">
                    <p>Este presupuesto no tiene líneas.</p>
                </c:when>
                <c:otherwise>
                    <ol>
                        <c:forEach var="linea" items="${lineasPresupuesto}">
                            <li>
                                <strong><c:out value="${linea.nombrePieza}" default="Pieza" /></strong>
                                (#<c:out value="${linea.piezaId}" />)
                                <dl>
                                    <dt>Unidades</dt>
                                    <dd><c:out value="${linea.unidades}" /></dd>
                                    <dt>Precio unitario</dt>
                                    <dd><c:out value="${linea.precioUnitario}" default="—" /></dd>
                                    <dt>Importe</dt>
                                    <dd><c:out value="${linea.precioFinal}" default="—" /></dd>
                                </dl>
                            </li>
                        </c:forEach>
                    </ol>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

    <c:url var="urlVolverPresupuestos" value="/presupuesto">
        <c:param name="action" value="search" />
        <c:if test="${param.buscar eq '1'}">
            <c:param name="buscar" value="1" />
            <c:param name="numero" value="${param.numero}" />
            <c:param name="nombre" value="${param.nombre}" />
            <c:param name="estado" value="${param.estado}" />
        </c:if>
    </c:url>
    <a href="<c:out value='${urlVolverPresupuestos}' />">
        Volver a la búsqueda
    </a>
</section>

<%@ include file="/html/public/common/footer.jsp" %>
