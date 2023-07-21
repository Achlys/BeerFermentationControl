<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
        <title><spring:message code="title" /></title>
    </head>
    <body>
        <header>
            <div class="jumbotron text-center">
                <div class="dropdown">
                    <a class="dropdown-toggle" href="#" id="Dropdown" role="button" data-mdb-toggle="dropdown" aria-expanded="false">
                        <spring:message code="language" var="language" />
                        <c:choose>
                            <c:when test="${language eq 'es'}">
                                <i class="flag-european-union flag m-0"></i>Español
                            </c:when>
                            <c:when test="${language eq 'eu'}">
                                <i class="flag-spain flag m-0"></i>Euskara
                            </c:when>
                            <c:otherwise>
                                <i class="flag-united-kingdom flag m-0"></i>English
                            </c:otherwise>
                        </c:choose>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="Dropdown">
                        <li>
                            <a class="dropdown-item" href="#">
                                <i class="flag-european-union flag"></i>Euskara
                                <c:if test="${language eq 'eu'}">
                                    <i class="fa fa-check text-success ms-2"></i>
                                </c:if>
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">
                                <i class="flag-united-kingdom flag"></i>English 
                                <c:if test="${language ne 'eu' and language ne 'es'}">
                                    <i class="fa fa-check text-success ms-2"></i>
                                </c:if>
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">
                                <i class="flag-spain flag"></i>Español
                                <c:if test="${language eq 'es'}">
                                    <i class="fa fa-check text-success ms-2"></i>
                                </c:if>
                            </a>
                        </li>
                    </ul>
                </div>
                <h1><spring:message code="h1.login" /></h1>
                <p><spring:message code="subtitle.login" /></p>
            </div>
        </header>
        <section>
            <form:form action="/login" modelAttribute="usuario">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-12">

                        </div>
                    </div>
                </div>
            </form:form>
        </section>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
    </body>
</html>