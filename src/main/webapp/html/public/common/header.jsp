
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.paula.checkmc.model.*" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">

    <link rel="stylesheet"
      href="<%=request.getContextPath()%>/css/style.css?">

    <title>CheckMyCar</title>
</head>

<body>

<header>

    <!-- LOGO -->
    <a class="logo"
       href="<%=request.getContextPath()%>/html/public/index.jsp">

        <img src="<%=request.getContextPath()%>/img/logo.png"
             width="150"
             alt="Logo CheckMyCar">
    </a>

    <!-- MENÚ PRINCIPAL -->
    <nav id="main-menu">

        <a href="<%=request.getContextPath()%>/html/public/index.jsp">
            Inicio
        </a>

    
<!-- DESPLEGABLE SERVICIOS -->
<details class="dropdown-servicios">

    <summary class="btn-servicios">
        Servicios
        <span class="flecha-servicios">▾</span>
    </summary>

    <div class="menu-servicios">

        <h3>Servicios</h3>

        <a href="<%=request.getContextPath()%>/html/private/presupuesto/presupuestoSearch.jsp">
            Consultar presupuestos
        </a>

        <div class="titulo-servicio">
            Solicitar cita
        </div>

        <div class="separador-servicios"></div>

        <h3>Gestión del taller</h3>

        <div class="titulo-servicio">
            Gestión del catálogo
        </div>

        <a href="<%=request.getContextPath()%>/controller/public/catalogo">
            Publicar vehículo
        </a>

        <div class="titulo-servicio">
            Gestión de clientes
        </div>

        <div class="titulo-servicio">
            Gestión de presupuestos
        </div>

        <div class="titulo-servicio">
            Gestión de órdenes de trabajo
        </div>

        <div class="titulo-servicio">
            Gestión de citas
        </div>

    </div>

</details>
     

        <a href="<%=request.getContextPath()%>/html/public/vehiculo/vehiculosView.jsp">
            Vehículos en venta
        </a>

        <a href="<%=request.getContextPath()%>/html/public/conocenos/conocenosView.jsp">
            Conócenos
        </a>

    </nav>

    <!-- MENÚ DEL USUARIO -->
    <div id="user-menu">

        <%@ include file="/html/public/usuarios/user-menu.jsp" %>

    </div>

</header>

<!-- CONTENIDO DE CADA PÁGINA -->
<div class="content">
