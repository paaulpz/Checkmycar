
<%@ include file="/html/public/common/header.jsp" %>

<%
    PresupuestoDTO presupuesto = (PresupuestoDTO) request.getAttribute("presupuesto");
    out.println("<h1>" + presupuesto.getNombre() + "</h1>");
    out.println("<p>Precio final: " + presupuesto.getPrecioFinal() + " </p>");
    out.println("<p>Fecha: " + presupuesto.getFecha() + "</p>");
    out.println("<p>Estado: " + presupuesto.getEstadoPresupuestoId() + "</p>");
%>

<%@ include file="/html/public/common/footer.jsp" %>

