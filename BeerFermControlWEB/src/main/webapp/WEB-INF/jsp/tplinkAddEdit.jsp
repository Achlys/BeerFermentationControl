<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.xer.beerfermcontrol.web.util.WebConstants" %>
<%@ page import="org.xer.beerfermcontrol.core.util.CoreConstants" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                            <c:if test="${requestScope[WebConstants.TPLINK].type eq CoreConstants.TPLINK_TYPE_COLD}">
                                <c:if test="${empty requestScope[WebConstants.TPLINK].id}">
                                    <spring:url value="/config/${requestScope[WebConstants.TPLINK].configId}/tplink/add/cold" var="URL_ADD_EDIT_TPLINK" />
                                </c:if>
                                <spring:message code="cold.plug" var="TYPE_DESC" />
                            </c:if>
                            <c:if test="${requestScope[WebConstants.TPLINK].type eq CoreConstants.TPLINK_TYPE_WARM}">
                                <c:if test="${empty requestScope[WebConstants.TPLINK].id}">
                                    <spring:url value="/config/${requestScope[WebConstants.TPLINK].configId}/tplink/add/warm" var="URL_ADD_EDIT_TPLINK" />
                                </c:if>
                                <spring:message code="warm.plug" var="TYPE_DESC" />
                            </c:if>
                            <c:if test="${not empty requestScope[WebConstants.TPLINK].id}">
                                <spring:url value="/config/${requestScope[WebConstants.TPLINK].configId}/tplink/${requestScope[WebConstants.TPLINK].id}/edit" var="URL_ADD_EDIT_TPLINK" />
                            </c:if>
                            <div class="dropdown-menu float-right" aria-labelledby="navbarDropdownMenuLink">
                                <c:choose>
                                    <c:when test="${language eq 'es'}">
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=eu">Euskara</a>
                                        <a class="dropdown-item active" href="#">Español</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=en">English</a>
                                    </c:when>
                                    <c:when test="${language eq 'eu'}">
                                        <a class="dropdown-item active" href="#">Euskara</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=es">Español</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=en">English</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=eu">Euskara</a>
                                        <a class="dropdown-item" href="${URL_ADD_EDIT_TPLINK}?language=es">Español</a>
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
            <form:form action="${URL_ADD_EDIT_TPLINK}" modelAttribute="${WebConstants.TPLINK}">
                <div class="mt-5">
                    <div class="row">
                        <div class="col-sm-8">
                            <h2>
                                <c:if test="${empty requestScope[WebConstants.TPLINK].id}">
                                    <spring:message code="add.tplink" />
                                </c:if>
                                <c:if test="${not empty requestScope[WebConstants.TPLINK].id}">
                                    <spring:message code="edit.tplink" />
                                </c:if>
                            </h2>
                        </div>
                        <div class="col-sm-4">
                            <div class="btn-group float-right" role="group">
                                <button type="submit" class="btn btn-primary"><i class="fa fa-floppy-disk"></i> <spring:message code="save" /></button>
                                <spring:url value="/config/${requestScope[WebConstants.TPLINK].configId}" var="URL_BACK" />
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
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label path="typePlain"><spring:message code="type" /></label>
                            <input type="text" id="typePlain" class="form-control-plaintext" readonly value="${TYPE_DESC}" />
                        </div>
                        <div class="form-group col-md-8">
                            <form:label path="ip"><spring:message code="ip" /></form:label>
                            <form:input path="ip" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" pattern="\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}" placeholder="###.###.###.###" />
                            <form:errors path="ip" cssClass="invalid-feedback" />
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <form:label path="email"><spring:message code="email" /></form:label>
                            <form:input type="email" path="email" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" />
                            <form:errors path="email" cssClass="invalid-feedback" />
                        </div>
                        <div class="form-group col-md-6">
                            <form:label path="password"><spring:message code="password" /></form:label>
                            <form:input type="password" path="password" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" />
                            <form:errors path="password" cssClass="invalid-feedback" />
                        </div>
                    </div>
                </div>
            </form:form>
        </main>
        <script src="${URL_STATIC}/js/jquery-3.7.0.min.js"></script>
        <script src="${URL_STATIC}/js/jquery-ui.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
    </body>
</html>
