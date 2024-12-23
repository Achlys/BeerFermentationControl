<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.xer.beerfermcontrol.web.util.WebConstants" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <spring:url value="/static" var="URL_STATIC" />
        <link rel="stylesheet" href="${URL_STATIC}/css/bootstrap.min.css">
        <link rel="stylesheet" href="${URL_STATIC}/css/jquery-ui.min.css">
        <link rel="stylesheet" href="${URL_STATIC}/css/beer_ferm_control.css">
        <link rel="icon" href="${URL_STATIC}/images/beer-icon.png">
        <script src="${URL_STATIC}/js/fontawesome_23c13a6a20.js"></script>
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
                            <c:if test="${empty requestScope[WebConstants.HYDROM].id}">
                                <spring:url value="/config/${requestScope[WebConstants.HYDROM].configId}/hydrom/add" var="URL_ADD_EDIT_HYDROM" />
                            </c:if>
                            <c:if test="${not empty requestScope[WebConstants.HYDROM].id}">
                                <spring:url value="/config/${requestScope[WebConstants.HYDROM].configId}/hydrom/${requestScope[WebConstants.HYDROM].id}/edit" var="URL_ADD_EDIT_HYDROM" />
                            </c:if>
                            <div class="dropdown-menu float-right" aria-labelledby="navbarDropdownMenuLink">
                                <c:choose>
                                    <c:when test="${language eq 'es'}">
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=eu">Euskara</a>
                                        <a class="dropdown-item active" href="#">Español</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=en">English</a>
                                    </c:when>
                                    <c:when test="${language eq 'eu'}">
                                        <a class="dropdown-item active" href="#">Euskara</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=es">Español</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=en">English</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=eu">Euskara</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_HYDROM}?language=es">Español</a>
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
            <form:form action="${URL_ADD_EDIT_HYDROM}" modelAttribute="${WebConstants.HYDROM}">
                <div class="mt-5">
                    <div class="row">
                        <div class="col-sm-8">
                            <h2>
                                <c:if test="${empty requestScope[WebConstants.HYDROM].id}">
                                    <spring:message code="add.hydrom" />
                                </c:if>
                                <c:if test="${not empty requestScope[WebConstants.HYDROM].id}">
                                    <spring:message code="edit.hydrom" />
                                </c:if>
                            </h2>
                        </div>
                        <div class="col-sm-4">
                            <div class="btn-group float-right" role="group">
                                <button type="submit" class="btn btn-primary"><i class="fa fa-floppy-disk"></i> <spring:message code="save" /></button>
                                <spring:url value="/config/${requestScope[WebConstants.HYDROM].configId}" var="URL_BACK" />
                                <a class="btn btn-secondary" href="${URL_BACK}"><i class="fa fa-xmark"></i> <spring:message code="cancel" /></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mt-3">
                    <div class="form-row">
                        <div class="form-group col-md-12">
                            <form:label path="name"><spring:message code="name" /></form:label>
                            <form:input path="name" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" />
                            <form:errors path="name" cssClass="invalid-feedback" />
                        </div>
                    </div>
                </div>
            </form:form>
        </main>
        <script src="${URL_STATIC}/js/jquery-3.7.0.min.js"></script>
        <script src="${URL_STATIC}/js/jquery-ui.min.js"></script>
        <script src="${URL_STATIC}/js/popper.min.jspopper.min.js"></script>
        <script src="${URL_STATIC}/js/bootstrap.min.js"></script>
    </body>
</html>
