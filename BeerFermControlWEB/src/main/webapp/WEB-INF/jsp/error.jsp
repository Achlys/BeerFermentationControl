<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <spring:url value="/static" var="URL_STATIC" />
        <link rel="stylesheet" href="${URL_STATIC}/css/bootstrap.min.css">
        <link rel="icon" href="${URL_STATIC}/images/beer-icon.png">
        <link rel="stylesheet" href="${URL_STATIC}/css/beer_ferm_control.css">
        <title><spring:message code="title" /></title>
    </head>
    <body>
        <header
            <div class="jumbotron text-center bg-danger text-white">
                <h1><spring:message code="h1.error" /></h1>
            </div>
        </header>
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <p><spring:message code="p.1.error" /></p>
                        <!--
                        <c:out value="${ex.message}" />
                        <c:forEach var="trace" items="${ex.stackTrace}">
                            <c:out value="${trace}" />
                        </c:forEach>
                        -->
                        <spring:url value="/configList" var="URL_HOME" />
                        <p><a href="${URL_HOME}" class="btn btn-primary"><spring:message code="home.page" /></a></p>
                    </div>
                </div>
            </div>
        </section>
        <script src="${URL_STATIC}/js/jquery-3.7.0.min.js"></script>
        <script src="${URL_STATIC}/js/jquery-ui.min.js"></script>
        <script src="${URL_STATIC}/js/popper.min.jspopper.min.js"></script>
        <script src="${URL_STATIC}/js/bootstrap.min.js"></script>
    </body>
</html>