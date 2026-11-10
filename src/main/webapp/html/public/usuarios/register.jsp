<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - Checkmycar</title>
</head>
<body>

    <h1>Registrar cliente</h1>

    <p style="color: red;">${registerError}</p>

    <form action="${pageContext.request.contextPath}/controller/public/usuarios"
          method="post" enctype="multipart/form-data">

        <input type="hidden" name="action" value="register">

        <label for="login">E-mail:</label>
        <input type="email" id="login" name="login" required><br>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required><br>

        <label for="dni_nie">DNI/NIE:</label>
        <input type="text" id="dni_nie" name="dni_nie" required><br>

        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required><br>

        <label for="apellido1">Primer apellido:</label>
        <input type="text" id="apellido1" name="apellido1" required><br>

        <label for="apellido2">Segundo apellido:</label>
        <input type="text" id="apellido2" name="apellido2"><br>

        <label for="telefono">Telefono:</label>
        <input type="tel" id="telefono" name="telefono"><br>

        <label for="direccion">Direccion:</label>
        <input type="text" id="direccion" name="direccion"><br>
        
        <label>Localidad:</label>
        <input type="number" name="localidadId" required>

        <label for="foto">Foto de perfil:</label>
        <input type="file" id="foto" name="foto-perfil" accept="image/jpeg,image/png"><br>

		<label>Género:</label>
		<input type="number" name="generoId" required>

        <input type="submit" value="Registrarse">
    </form>

    <p>
        <a href="${pageContext.request.contextPath}/controller/public/usuarios?action=login">
            Volver al login
        </a>
    </p>

</body>
</html>
