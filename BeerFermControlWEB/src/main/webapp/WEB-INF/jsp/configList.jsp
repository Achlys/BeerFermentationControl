<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.xer.beerfermcontrol.web.util.WebConstants" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
        <spring:url value="/static" var="URL_STATIC" />
        <link rel="stylesheet" href="${URL_STATIC}/css/jquery-ui.min.css">
        <link rel="stylesheet" href="${URL_STATIC}/css/beer_ferm_control.css">
        <link rel="icon" href="${URL_STATIC}/images/beer-icon.png">
        <script src="https://kit.fontawesome.com/23c13a6a20.js" crossorigin="anonymous"></script>
        <title><spring:message code="title" /></title>
    </head>
    <body>
        <header>
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <spring:url value="/configList" var="URL_INDEX" />
                <a class="navbar-brand" href="${URL_INDEX}">
                    <img src="${URL_STATIC}/images/beer-icon.png" width="30" height="30" alt="">
                </a>
                <span class="navbar-brand mb-0 h1"><spring:message code="title" /></span>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item active">&nbsp;</li>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fa-solid fa-flag"></i>
                                <spring:message code="language" var="language" />
                                <c:choose>
                                    <c:when test="${language eq 'es'}">
                                        Español
                                    </c:when>
                                    <c:when test="${language eq 'eu'}">
                                        Euskara
                                    </c:when>
                                    <c:otherwise>
                                        English
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="dropdown-menu float-right" aria-labelledby="navbarDropdownMenuLink">
                                <spring:url value="/configList?language=" var="URL_LOCALE_CHANGE" />
                                <c:choose>
                                    <c:when test="${language eq 'es'}">
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}eu">Euskara</a>
                                        <a class="dropdown-item active" href="#">Español</a>
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}en">English</a>
                                    </c:when>
                                    <c:when test="${language eq 'eu'}">
                                        <a class="dropdown-item active" href="#">Euskara</a>
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}es">Español</a>
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}en">English</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}eu">Euskara</a>
                                        <a class="dropdown-item" href="${URL_LOCALE_CHANGE}es">Español</a>
                                        <a class="dropdown-item active" href="#">English</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </li>
                        <li class="nav-item">
                            <spring:url value="/logout" var="URL_LOGOUT" />
                            <a class="nav-link" href="${URL_LOGOUT}">
                                <i class="fa-solid fa-power-off"></i>
                                <spring:message code="logout" />
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
        <main class="container-lg">
            <div class="mt-5">
                <c:if test="${not empty requestScope[WebConstants.SUCCES_KEY]}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <spring:message code="${requestScope[WebConstants.SUCCES_KEY]}" />
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </c:if>
                <div class="row">
                    <div class="col-sm-8">
                        <h2><spring:message code="config.list" /></h2>
                    </div>
                    <div class="col-sm-4">
                        <spring:url value="/config" var="URL_CONFIG" />
                        <a class="btn btn-primary float-right" href="${URL_CONFIG}/add"><i class="fa fa-plus"></i> <spring:message code="add.new" /></a>
                    </div>
                </div>
                <c:if test="${empty requestScope[WebConstants.CONFIG_LIST]}">
                    <div class="alert alert-warning mt-3" role="alert">
                        <spring:message code="empty.list" />
                    </div>
                </c:if>
                <c:if test="${not empty requestScope[WebConstants.CONFIG_LIST]}">
                    <div class="mt-3">
                        <table id="configList" class="table table-striped table-bordered" style="width:100%">
                            <thead class="thead-dark">
                                <tr>
                                    <th><spring:message code="name" /></th>
                                    <th><spring:message code="start.date" /></th>
                                    <th><spring:message code="end.date" /></th>
                                    <th><spring:message code="tolerance" /></th>
                                    <th><spring:message code="cold.plug" /></th>
                                    <th><spring:message code="warm.plug" /></th>
                                    <th><spring:message code="hydrom" /></th>
                                    <th>&nbsp;</th>
                                </tr>
                            </thead>
                            <tbody>
                                <spring:message code="date.pattern" var="DATE_PATTERN" />
                                <c:forEach items="${requestScope[WebConstants.CONFIG_LIST]}" var="config">
                                    <tr>
                                        <td><c:out value="${config.name}" /></td>
                                        <td><fmt:formatDate pattern="${DATE_PATTERN}" value="${config.startDate}" /></td>
                                        <td><fmt:formatDate pattern="${DATE_PATTERN}" value="${config.endDate}" /></td>
                                        <td><fmt:formatNumber pattern="#.##" value="${config.tolerance}" /></td>
                                        <td>
                                            <c:if test="${empty config.tplinkCold}">-</c:if>
                                            <c:if test="${not empty config.tplinkCold}"><c:out value="config.tplinkCold.name" /></c:if>
                                            </td>
                                            <td>
                                            <c:if test="${empty config.tplinkWarm}">-</c:if>
                                            <c:if test="${not empty config.tplinkWarm}"><c:out value="config.tplinkWarm.name" /></c:if>
                                            </td>
                                            <td>
                                            <c:if test="${empty config.hydrom}">-</c:if>
                                            <c:if test="${not empty config.hydrom}"><c:out value="config.hydrom.name" /></c:if>
                                            </td>
                                            <td>
                                                <a href="${URL_CONFIG}/${config.id}" class="btn btn-primary btn-sm"><i class="far fa-eye"></i></a>
                                            <a href="${URL_CONFIG}/${config.id}/edit" class="btn btn-success btn-sm"><i class="fas fa-edit"></i></a>
                                            <a href="${URL_CONFIG}/${config.id}/remove" class="btn btn-danger btn-sm"><i class="far fa-trash-alt"></i></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </main>
        <script src="${URL_STATIC}/js/jquery-3.7.0.min.js"></script>
        <script src="${URL_STATIC}/js/jquery-ui.min.js"></script>
        <c:if test="${language eq 'es'}">
            <script src="${URL_STATIC}/js/datepicker-es.js"></script>
        </c:if>
        <c:if test="${language eq 'eu'}">
            <script src="${URL_STATIC}/js/datepicker-eu.js"></script>
        </c:if>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
        <script>
            $.datepicker.setDefaults($.datepicker.regional[ "${language}" ]);
            $(function () {
                new DataTable('#configList');
            });
        </script>
    </body>
</html>
