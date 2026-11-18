<%@ include file="/html/public/common/header.jsp"%>
<%@ include file = "/html/private/presupuesto/presupuestoSearch.jsp"%>

<!--  Resultados de busqueda -->
<c:set var="results" value="${requestScope.results}" />
<c:if test= "${not empty results}" >
<div class="presupuesto-resultados">
<p> Encontrados ${results.total} resultados </p>
<ol>
<c:forEach var = "e" items="${results.page}">
<c:url var="urlDetalle" value="/presupuesto">
    <c:param name="action" value="detail" />
    <c:param name="id" value="${e.id}" />
</c:url>
<li><a href="<c:out value='${urlDetalle}' />"><c:out value="${e.nombre}" /></a></li>
</c:forEach>
</ol>
</div>
</c:if>
<%@ include file="/html/public/common/footer.jsp"%>
