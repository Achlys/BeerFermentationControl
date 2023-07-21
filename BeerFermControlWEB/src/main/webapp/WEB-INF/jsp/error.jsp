<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><spring:message code="title.error" /></title>
    </head>
    <body>
        <header>
            <h1><spring:message code="h1.error" /></h1>
        </header>
        <content>
            <p><spring:message code="p.1.error" /></p>
            <!--
<c:out value="${ex.message}" />
<c:forEach var="trace" items="${ex.stackTrace}">
    <c:out value="${trace}" />
</c:forEach>
            -->
        </content>
    </body>
</html>
