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
        <link rel="stylesheet" href="${URL_STATIC}/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="${URL_STATIC}/css/responsive.bootstrap4.min.css">
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
                                <c:if test="${empty param[WebConstants.TAB]}">
                                    <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}?language=" var="URL_LOCALE_CHANGE" />
                                </c:if>
                                <c:if test="${not empty param[WebConstants.TAB]}">
                                    <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}?${WebConstants.TAB}=${requestScope[WebConstants.TAB]}&language=" var="URL_LOCALE_CHANGE" />
                                </c:if>
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
                        <h2><spring:message code="config.details" /></h2>
                    </div>
                    <div class="col-sm-4">
                        <div class="btn-group float-right" role="group">
                            <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/edit" var="URL_EDIT_CONFIG" />
                            <a href="${URL_EDIT_CONFIG}" class="btn btn-primary"><i class="fas fa-edit"></i> <spring:message code="edit" /></a>
                            <a class="btn btn-secondary" href="${URL_INDEX}"><i class="fa-solid fa-arrow-left"></i> <spring:message code="back" /></a>
                        </div>
                    </div>
                </div>
            </div>
            <c:choose>
                <c:when test="${param[WebConstants.TAB] eq WebConstants.TAB_RANGES}">
                    <c:set var="RANGES_TAB_ACTIVE" value="active" />
                    <c:set var="RANGES_TAB_SHOW" value="show" />
                </c:when>
                <c:when test="${param[WebConstants.TAB] eq WebConstants.TAB_READINGS}">
                    <c:set var="READINGS_TAB_ACTIVE" value="active" />
                    <c:set var="READINGS_TAB_SHOW" value="show" />
                </c:when>
                <c:when test="${param[WebConstants.TAB] eq WebConstants.TAB_EVENTS}">
                    <c:set var="EVENTS_TAB_ACTIVE" value="active" />
                    <c:set var="EVENTS_TAB_SHOW" value="show" />
                </c:when>
                <c:otherwise>
                    <c:set var="HOME_TAB_ACTIVE" value="active" />
                    <c:set var="HOME_TAB_SHOW" value="show" />
                </c:otherwise>
            </c:choose>
            <ul class="nav nav-tabs mt-3" id="myTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${HOME_TAB_ACTIVE}" id="home-tab" data-toggle="tab" data-target="#home" type="button" role="tab" aria-controls="home" aria-selected="true">
                        <spring:message code="details" />
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${RANGES_TAB_ACTIVE}" id="ranges-tab" data-toggle="tab" data-target="#ranges" type="button" role="tab" aria-controls="ranges" aria-selected="false">
                        <spring:message code="ranges" />
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${READINGS_TAB_ACTIVE}" id="readings-tab" data-toggle="tab" data-target="#readings" type="button" role="tab" aria-controls="readings" aria-selected="false">
                        <spring:message code="readings" />
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${EVENTS_TAB_ACTIVE}" id="events-tab" data-toggle="tab" data-target="#events" type="button" role="tab" aria-controls="events" aria-selected="false">
                        <spring:message code="events" />
                    </button>
                </li>
            </ul>
            <spring:message code="date.pattern" var="DATE_PATTERN" />
            <spring:message code="timestamp.pattern" var="TIMESTAMP_PATTERN" />
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade ${HOME_TAB_SHOW} ${HOME_TAB_ACTIVE}" id="home" role="tabpanel" aria-labelledby="home-tab">
                    <div class="form-row mt-3">
                        <div class="form-group col-md-12">
                            <label for="name"><spring:message code="name" /></label>
                            <input id="name" type="text" class="form-control" readonly value="${requestScope[WebConstants.CONFIG].name}" />
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label path="tolerance"><spring:message code="tolerance" /></label>
                            <input id="tolerance" type="number" class="form-control" readonly value="${requestScope[WebConstants.CONFIG].tolerance}" />
                        </div>
                        <div class="form-group col-md-4">
                            <label path="startDate"><spring:message code="start.date" /></label>
                            <fmt:formatDate pattern="${DATE_PATTERN}" value="${requestScope[WebConstants.CONFIG].startDate}" var="START_DATE" />
                            <input id="startDate" type="text" class="form-control" readonly value="${START_DATE}" />
                        </div>
                        <div class="form-group col-md-4">
                            <label path="endDate"><spring:message code="end.date" /></label>
                            <fmt:formatDate pattern="${DATE_PATTERN}" value="${requestScope[WebConstants.CONFIG].endDate}" var="END_DATE" />
                            <input id="endDate" type="text" class="form-control" readonly value="${END_DATE}" />
                        </div>
                    </div>
                    <div class="form-row">
                        <c:if test="${empty requestScope[WebConstants.CONFIG].hydrom}">
                            <c:set value="-" var="HYDROM_NAME" />
                            <c:set value="10" var="HYDROM_NAME_WIDTH" />
                            <c:set value="2" var="HYDROM_BUTTONS_WIDTH" />
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].hydrom}">
                            <c:set value="${requestScope[WebConstants.CONFIG].hydrom.name}" var="HYDROM_NAME" />
                            <c:set value="8" var="HYDROM_NAME_WIDTH" />
                            <c:set value="4" var="HYDROM_BUTTONS_WIDTH" />
                        </c:if>
                        <div class="form-group col-md-${HYDROM_NAME_WIDTH}">
                            <label path="hydrom.name"><spring:message code="hydrom" /></label>
                            <input id="hydrom.name" type="text" class="form-control" readonly value="${HYDROM_NAME}" />
                        </div>
                        <c:if test="${empty requestScope[WebConstants.CONFIG].hydrom}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/hydrom/add" var="URL_ADD_HYDROM" />
                                <a href="${URL_ADD_HYDROM}" class="btn btn-primary btn-block"><i class="fa fa-plus"></i> <spring:message code="add" /></a>
                            </div>
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].hydrom}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/hydrom/${requestScope[WebConstants.CONFIG].hydrom.id}/edit" var="URL_EDIT_HYDROM" />
                                <a href="${URL_EDIT_HYDROM}" class="btn btn-primary btn-block"><i class="fas fa-edit"></i> <spring:message code="edit" /></a>
                            </div>
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/hydrom/${requestScope[WebConstants.CONFIG].hydrom.id}/remove" var="URL_REMOVE_HYDROM" />
                                <spring:message code="ask.remove.hydrom" var="REMOVE_HYDROM_MESSAGE" />
                                <a href="javascript:fncShowModal('${URL_REMOVE_HYDROM}', '${REMOVE_HYDROM_MESSAGE}');" class="btn btn-danger btn-block"><i class="far fa-trash-alt"></i> <spring:message code="remove" /></a>
                            </div>
                        </c:if>
                    </div>
                    <div class="form-row">
                        <c:if test="${empty requestScope[WebConstants.CONFIG].tplinkCold}">
                            <c:set value="-" var="TPLINC_COLD_NAME" />
                            <c:set value="10" var="TPLINC_COLD_NAME_WIDTH" />
                            <c:set value="2" var="TPLINC_COLD_BUTTONS_WIDTH" />
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].tplinkCold}">
                            <c:set value="${requestScope[WebConstants.CONFIG].tplinkCold.name}" var="TPLINC_COLD_NAME" />
                            <c:set value="8" var="TPLINC_COLD_NAME_WIDTH" />
                            <c:set value="4" var="TPLINC_COLD_BUTTONS_WIDTH" />
                        </c:if>
                        <div class="form-group col-md-${TPLINC_COLD_NAME_WIDTH}">
                            <label path="tplink.cold.name"><spring:message code="tplink.cold" /></label>
                            <input id="tplink.cold.name" type="text" class="form-control" readonly value="${TPLINC_COLD_NAME}" />
                        </div>
                        <c:if test="${empty requestScope[WebConstants.CONFIG].tplinkCold}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/add/cold" var="URL_ADD_TPLINC_COLD" />
                                <a href="${URL_ADD_TPLINC_COLD}" class="btn btn-primary btn-block"><i class="fa fa-plus"></i> <spring:message code="add" /></a>
                            </div>
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].tplinkCold}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/${requestScope[WebConstants.CONFIG].tplinkCold.id}/edit" var="URL_EDIT_TPLINC_COLD" />
                                <a href="${URL_EDIT_TPLINC_COLD}" class="btn btn-primary btn-block"><i class="fas fa-edit"></i> <spring:message code="edit" /></a>
                            </div>
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/${requestScope[WebConstants.CONFIG].tplinkCold.id}/remove" var="URL_REMOVE_TPLINC_COLD" />
                                <spring:message code="ask.remove.tplink" var="REMOVE_TPLINK_MESSAGE" />
                                <a href="javascript:fncShowModal('${URL_REMOVE_TPLINC_COLD}', '${REMOVE_TPLINK_MESSAGE}');" class="btn btn-danger btn-block"><i class="far fa-trash-alt"></i> <spring:message code="remove" /></a>
                            </div>
                        </c:if>
                    </div>
                    <div class="form-row mb-3">
                        <c:if test="${empty requestScope[WebConstants.CONFIG].tplinkWarm}">
                            <c:set value="-" var="TPLINC_WARM_NAME" />
                            <c:set value="10" var="TPLINC_WARM_NAME_WIDTH" />
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].tplinkWarm}">
                            <c:set value="${requestScope[WebConstants.CONFIG].tplinkWarm.name}" var="TPLINC_WARM_NAME" />
                            <c:set value="8" var="TPLINC_WARM_NAME_WIDTH" />
                        </c:if>
                        <div class="form-group col-md-${TPLINC_WARM_NAME_WIDTH}">
                            <label path="tplink.warm.name"><spring:message code="tplink.warm" /></label>
                            <input id="tplink.warm.name" type="text" class="form-control" readonly value="${TPLINC_WARM_NAME}" />
                        </div>
                        <c:if test="${empty requestScope[WebConstants.CONFIG].tplinkWarm}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/add/warm" var="URL_ADD_TPLINC_WARM" />
                                <a href="${URL_ADD_TPLINC_WARM}" class="btn btn-primary btn-block"><i class="fa fa-plus"></i> <spring:message code="add" /></a>
                            </div>
                        </c:if>
                        <c:if test="${not empty requestScope[WebConstants.CONFIG].tplinkWarm}">
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/${requestScope[WebConstants.CONFIG].tplinkWarm.id}/edit" var="URL_EDIT_TPLINC_WARM" />
                                <a href="${URL_EDIT_TPLINC_WARM}" class="btn btn-primary btn-block"><i class="fas fa-edit"></i> <spring:message code="edit" /></a>
                            </div>
                            <div class="form-group col-md-2">
                                <label>&nbsp;</label>
                                <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/tplink/${requestScope[WebConstants.CONFIG].tplinkWarm.id}/remove" var="URL_REMOVE_TPLINC_WARM" />
                                <a href="javascript:fncShowModal('${URL_REMOVE_TPLINC_WARM}', '${REMOVE_TPLINK_MESSAGE}');" class="btn btn-danger btn-block"><i class="far fa-trash-alt"></i> <spring:message code="remove" /></a>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="tab-pane fade ${RANGES_TAB_SHOW} ${RANGES_TAB_ACTIVE}" id="ranges" role="tabpanel" aria-labelledby="ranges-tab">
                    <div class="row mt-3">
                        <div class="col-sm-8">
                            <h3><spring:message code="range.list" /></h3>
                        </div>
                        <div class="col-sm-4">
                            <spring:url value="/config/${requestScope[WebConstants.CONFIG].id}/range" var="URL_RANGE" />
                            <a class="btn btn-primary float-right" href="${URL_RANGE}/add"><i class="fa fa-plus"></i> <spring:message code="add.new" /></a>
                        </div>
                    </div>
                    <c:if test="${empty requestScope[WebConstants.CONFIG].ranges}">
                        <div class="alert alert-warning mt-3" role="alert">
                            <spring:message code="empty.list" />
                        </div>
                    </c:if>
                    <c:if test="${not empty requestScope[WebConstants.CONFIG].ranges}">
                        <div class="mt-3 mb-3">
                            <spring:message code="ask.remove.range" var="REMOVE_RANGE_MESSAGE" />
                            <table id="rangeList" class="table table-striped table-bordered nowrap" style="width: 100%;">
                                <thead class="thead-dark">
                                    <tr>
                                        <th><spring:message code="top.gravity" /></th>
                                        <th><spring:message code="bottom.gravity" /></th>
                                        <th><spring:message code="aimed.temperature" /></th>
                                        <th>&nbsp;</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${requestScope[WebConstants.CONFIG].ranges}" var="range">
                                        <tr>
                                            <td><fmt:formatNumber pattern="0.000" value="${range.topGravity}" /></td>
                                            <td><fmt:formatNumber pattern="0.000" value="${range.bottomGravity}" /></td>
                                            <td><fmt:formatNumber pattern="0.#" value="${range.aimedTemp}" /></td>
                                            <td class="text-center">
                                                <a href="${URL_RANGE}/${range.id}/edit" class="btn btn-success btn-sm"><i class="fas fa-edit"></i></a>
                                                <a href="javascript:fncShowModal('${URL_RANGE}/${range.id}/remove', '${REMOVE_RANGE_MESSAGE}');" class="btn btn-danger btn-sm"><i class="far fa-trash-alt"></i></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
                <div class="tab-pane fade ${READINGS_TAB_SHOW} ${READINGS_TAB_ACTIVE}" id="readings" role="tabpanel" aria-labelledby="readings-tab">
                    <div class="row mt-3">
                        <div class="col-sm-12">
                            <h3><spring:message code="reading.list" /></h3>
                        </div>
                    </div>
                    <c:if test="${empty requestScope[WebConstants.READING_LIST]}">
                        <div class="alert alert-warning mt-3" role="alert">
                            <spring:message code="empty.list" />
                        </div>
                    </c:if>
                    <c:if test="${not empty requestScope[WebConstants.READING_LIST]}">
                        <div class="mt-3 mb-3">
                            <table id="readingList" class="table table-striped table-bordered w-100 nowrap">
                                <thead class="thead-dark">
                                    <tr>
                                        <th><spring:message code="moment" /></th>
                                        <th><spring:message code="gravity" /></th>
                                        <th><spring:message code="temperature" /></th>
                                        <th><spring:message code="json" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${requestScope[WebConstants.READING_LIST]}" var="reading">
                                        <tr>
                                            <td><fmt:formatDate pattern="${TIMESTAMP_PATTERN}" value="${reading.moment}" /></td>
                                            <td><fmt:formatNumber pattern="0.000" value="${reading.gravity}" /></td>
                                            <td><fmt:formatNumber pattern="0.#" value="${reading.temp}" /></td>
                                            <td><c:out value="${reading.json}" /></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
                <div class="tab-pane fade ${EVENTS_TAB_SHOW} ${EVENTS_TAB_ACTIVE}" id="events" role="tabpanel" aria-labelledby="events-tab">
                    <div class="row mt-3">
                        <div class="col-sm-12">
                            <h3><spring:message code="event.list" /></h3>
                        </div>
                    </div>
                    <c:if test="${empty requestScope[WebConstants.EVENT_LIST]}">
                        <div class="alert alert-warning mt-3" role="alert">
                            <spring:message code="empty.list" />
                        </div>
                    </c:if>
                    <c:if test="${not empty requestScope[WebConstants.EVENT_LIST]}">
                        <div class="mt-3 mb-3">
                            <table id="eventList" class="table table-striped table-bordered w-100">
                                <thead class="thead-dark">
                                    <tr>
                                        <th><spring:message code="moment" /></th>
                                        <th><spring:message code="event" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${requestScope[WebConstants.EVENT_LIST]}" var="log">
                                        <tr>
                                            <td class="text-nowrap"><fmt:formatDate pattern="${TIMESTAMP_PATTERN}" value="${log.moment}" /></td>
                                            <td><c:out value="${log.event}" /></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="removeModal" data-backdrop="static" data-keyboard="false" tabindex="-1" aria-labelledby="removeLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="removeLabel"><spring:message code="remove" /></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p id="removeModalMessage">...</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal"><spring:message code="cancel" /></button>
                        <a id="removeModalButton" href="#" class="btn btn-danger"><i class="far fa-trash-alt"></i> <spring:message code="remove" /></a>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <script src="${URL_STATIC}/js/jquery-3.7.0.min.js"></script>
    <script src="${URL_STATIC}/js/jquery-ui.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
    <script src="${URL_STATIC}/js/jquery.dataTables.min.js"></script>
    <script src="${URL_STATIC}/js/dataTables.bootstrap4.min.js"></script>
    <script src="${URL_STATIC}/js/dataTables.responsive.min.js"></script>
    <script src="${URL_STATIC}/js/responsive.bootstrap4.min.js"></script>
    <script>
        <spring:message code="datatable.language.url" var="DATATABLE_LANG_URL" />
        var rangeTable;
        var readingTable;
        var eventTable;
        $(function () {
            rangeTable = new DataTable('#rangeList', {
                language: {url: '${DATATABLE_LANG_URL}'},
                responsive: true,
                columnDefs: [
                    {responsivePriority: 1, targets: 0},
                    {responsivePriority: 2, targets: 3}
                ]
            });
            readingTable = new DataTable('#readingList', {
                language: {url: '${DATATABLE_LANG_URL}'},
                responsive: true,
                "order": [[ 0, 'desc' ]]
            });
            eventTable = new DataTable('#eventList', {
                language: {url: '${DATATABLE_LANG_URL}'},
                responsive: true,
                "order": [[ 0, 'desc' ]]
            });

            $('#myTab button').on('click', function (event) {
                setTimeout(function(){
                    rangeTable.columns.adjust().draw();
                    readingTable.columns.adjust().draw();
                    eventTable.columns.adjust().draw();
                }, 100);
            });
        });

        function fncShowModal(url, message) {
            $("#removeModalMessage").html(message);
            $("#removeModalButton").attr('href', url);
            $("#removeModal").modal('show');
        }
    </script>
</body>
</html>
