<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.paula.checkmc.model.*" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <link rel="stylesheet"
        href="<%=request.getContextPath()%>/css/style.css">

    <title>CheckMyCar</title>
</head>

<body>

<header>

    <!-- LOGO -->
    <a class="logo" href="<%=request.getContextPath()%>/index.jsp">
        <img src="<%=request.getContextPath()%>/img/logo.png"
             width="150"
             alt="Logo CheckMyCar">
    </a>


    <!-- MENÚ PRINCIPAL -->
    <nav id="main-menu">

        <a href="<%=request.getContextPath()%>/html/public/index.jsp">
            Inicio
        </a>

        <a href="<%=request.getContextPath()%>/html/private/presupuesto/presupuestoSearch.jsp">
            Servicios
        </a>

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