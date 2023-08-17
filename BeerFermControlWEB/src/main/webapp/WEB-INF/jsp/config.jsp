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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
        <spring:url value="/static" var="URL_STATIC" />
        <link rel="icon" href="${URL_STATIC}/images/beer-icon.png">
        <link rel="stylesheet" href="${URL_STATIC}/css/beer_ferm_control.css">
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
                                <spring:url value="/config/xxx?language=" var="URL_LOCALE_CHANGE" />
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
            <div class="nav-scroller bg-white shadow-sm">
                <nav class="nav nav-underline">
                    <span class="nav-link active"></span>
                    
                </nav>
            </div>
        </header>
        <main class="container-lg">
            <div class="mt-5">
                <div class="row">
                    <div class="col-sm-8">
                        <h2><spring:message code="config.list" /></h2>
                    </div>
                    <div class="col-sm-4">
                        <spring:url value="/config/add" var="URL_ADD_CONFIG" />
                        <a class="btn btn-info add-new float-right" href="${URL_ADD_CONFIG}"><i class="fa fa-plus"></i> <spring:message code="add.new" /></a>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
    </body>
</html>
