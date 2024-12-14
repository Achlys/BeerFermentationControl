<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.xer.beerfermcontrol.web.util.WebConstants" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <spring:url value="/static" var="URL_STATIC" />
        <link rel="stylesheet" href="${URL_STATIC}/css/bootstrap.min.css">
        <link rel="stylesheet" href="${URL_STATIC}/css/beer_ferm_control.css">
        <link rel="icon" href="${URL_STATIC}/images/beer-icon.png">
        <script src="${URL_STATIC}/js/fontawesome_23c13a6a20.js"></script>
        <title><spring:message code="title" /></title>
    </head>
    <body>
        <main class="container-lg">
            <spring:url value="/login" var="URL_FORM" />
            <form:form action="${URL_FORM}" modelAttribute="${WebConstants.USER}" method="POST" cssClass="form-signin">
                <div class="mt-5">
                    <div class="row justify-content-center">
                        <div class="col-sm-4 text-center">
                            <img class="mb-4" src="${URL_STATIC}/images/beer-icon.png" alt="" width="72" height="72">
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-sm-4 text-center">
                            <h1 class="h3 mb-3 font-weight-normal"><spring:message code="subtitle.login" /></h1>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-4">
                            <spring:message code="username" var="i18n_username" />
                            <label for="username" class="sr-only"><c:out value="${i18n_username}" /></label>
                            <form:input path="username" placeholder="${i18n_username}" cssClass="form-control" required="" autofocus="" cssErrorClass="form-control is-invalid" />
                            <form:errors path="username" cssClass="invalid-feedback" />
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-4">
                            <spring:message code="password" var="i18n_password" />
                            <label for="password" class="sr-only"><c:out value="${i18n_password}" /></label>
                            <form:password path="password" placeholder="${i18n_password}" cssClass="form-control" required="" cssErrorClass="form-control is-invalid" />
                            <form:errors path="password" cssClass="invalid-feedback" />
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-sm-4">
                            <form:button type="submit" class="btn btn-lg btn-primary btn-block"><spring:message code="sign.in" /></form:button>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-sm-4 text-center">
                            <div class="dropdown mt-3">
                                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
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
                                </button>
                                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                    <spring:url value="/login?language=" var="URL_LOGIN" />
                                    <c:choose>
                                        <c:when test="${language eq 'es'}">
                                            <a class="dropdown-item" href="${URL_LOGIN}eu">Euskara</a>
                                            <a class="dropdown-item active" href="#">Español</a>
                                            <a class="dropdown-item" href="${URL_LOGIN}en">English</a>
                                        </c:when>
                                        <c:when test="${language eq 'eu'}">
                                            <a class="dropdown-item active" href="#">Euskara</a>
                                            <a class="dropdown-item" href="${URL_LOGIN}es">Español</a>
                                            <a class="dropdown-item" href="${URL_LOGIN}en">English</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="dropdown-item" href="${URL_LOGIN}eu">Euskara</a>
                                            <a class="dropdown-item" href="${URL_LOGIN}es">Español</a>
                                            <a class="dropdown-item active" href="#">English</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>  
                </div>       
            </form:form>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="${URL_STATIC}/js/popper.min.jspopper.min.js"></script>
        <script src="${URL_STATIC}/js/bootstrap.min.js"></script>
    </body>
</html>