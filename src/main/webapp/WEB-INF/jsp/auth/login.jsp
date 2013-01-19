<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>PhotoPile: Authentication</title>
</head>
<body>
    <h1>Photopile: Authentication</h1>

    <p>Please authenticate to access PhotoPile:</p>

    <spring:form commandName="credentials" method="POST">
        <spring:errors />
        <div>
            <label for="username">Username:</label>
            <spring:input path="username" id="username" />
            <spring:errors path="username" />
        </div>
        <div>
            <label for="password">Password:</label>
            <spring:password path="password" id="password" />
            <spring:errors path="password" />
        </div>
        <div>
            <label for="rememberMe">Remember Me</label>
            <spring:checkbox path="rememberMe" id="rememberMe" />
        </div>
        <button>Sign In</button>
    </spring:form>

</body>
</html>