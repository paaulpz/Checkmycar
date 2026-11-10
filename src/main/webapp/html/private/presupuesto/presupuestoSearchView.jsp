<%@ include file="/html/public/common/header.jsp"%>
<%@ include file = "/html/private/presupuesto/presupuestoSearch.jsp"%>

<!--  Resultados de busqueda -->
<c:set var= "results"value ="${requestScope [AttributeNames.RESULTS]}" />
<c:if test= "${not empty results}" >
<p> Encontrados {results.total} resultados </p>
<ol>
<c:forEach var = "e" items="${results.page}">
<li>${e.nombre}</li>
</c:forEach>
</ol>
</c:if>
<%@ include file="/html/public/common/footer.jsp"%>