<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Photopile: list</title>
</head>
<body>
<h1>Photopile</h1>

    <ol>
        <c:forEach items="${photos}" var="photo">
            <li>${photo}</li>
        </c:forEach>
    </ol>

</body>
</html>