<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CheckMyCar</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/loginstyle.css">
</head>

<body>

    <main>

        <section id="logo">
            <img src="${pageContext.request.contextPath}/img/logo.png"
                 alt="Logo de CheckMyCar">
        </section>

        <section id="login">

            <div class="contenido-login">

                <h2>Accede a tu cuenta</h2>

                <form action="${pageContext.request.contextPath}/controller/public/cliente"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="login">

                    <label for="dni">DNI</label>

                    <input type="text"
                           id="dni"
                           name="dni"
                           placeholder="Introduce tu DNI"
                           required>

                    <label for="password">Contraseña</label>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Introduce tu contraseña"
                           required>

                    <button type="submit">
                        Entrar
                    </button>

                </form>

                <a href="registro.html">
                    ¿No tienes cuenta?
                </a>

                <a href="${pageContext.request.contextPath}/html/public/index.jsp">
                    Volver al inicio
                </a>

            </div>

        </section>

    </main>

</body>

</html>