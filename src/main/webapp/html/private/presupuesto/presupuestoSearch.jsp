

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/presupuestos.css">

<form class="presupuesto-busqueda" action="<%=request.getContextPath()%>/presupuesto" method="post">

    <input type="hidden" name="action" value="search">
    <input type="text" name="nombre" aria-label="Nombre del presupuesto" placeholder="ex: U2">
    <input type="submit" value="Buscar">

</form>