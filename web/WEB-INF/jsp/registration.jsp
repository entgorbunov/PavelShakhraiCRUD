<%--
  Created by IntelliJ IDEA.
  User: ent
  Date: 6/10/24
  Time: 6:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%--
Created by IntelliJ IDEA.
User: ent
Date: 4/26/24
Time: 11:18 PM
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Registration Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap-theme.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container mt-5">
    <h2>Registration page</h2>
    <form action="${pageContext.request.contextPath}/registration" method="post"  class="needs-validation" novalidate>
        <div class="form-group">
            <label for="login">Login:</label>
            <input type="text" class="form-control" name="login" id="login" required>
        </div>
        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" name="name" id="name" required>
        </div>


        <button type="submit" class="btn btn-primary">Send</button>
    </form>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary mt-3">Login</a>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger mt-4">
            <c:forEach var="error" items="${errors}">
                <p>${error}</p>
            </c:forEach>
        </div>
    </c:if>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>

</html>

</body>
</html>
