<%--
  Created by IntelliJ IDEA.
  User: ent
  Date: 6/10/24
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MainPage</title>
</head>
<body>
<header>
    <a href="${pageContext.request.contextPath}/mainPage" class="header-link">CRUD</a>
    <div class="right-align">
        <a href="${pageContext.request.contextPath}/logout" class="header-link">Logout</a>
    </div>
</header>

<div>
    <h2>${user.name != null ? user.name : "User name not available"}</h2>
    <h2>${user.registrationDate != null ? user.registrationDate : "Registration date not available"}</h2>
</div>

</body>
</html>
