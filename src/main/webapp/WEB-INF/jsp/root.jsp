<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Welcome!</title>
    </head>
    <body>
        <h1>Photopile</h1>

        <p>${welcome}</p>

        <c:out value="${welcome}" default="I got nuttin" />

    </body>
</html>