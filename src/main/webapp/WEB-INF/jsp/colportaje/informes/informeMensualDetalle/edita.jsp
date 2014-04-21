<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeMensualDetalle.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensualDetalle" />
        </jsp:include>

        <div id="edita-informeMensualDetalle" class="content scaffold-list" role="main">
            <h1><s:message code="informeMensualDetalle.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/informes/informeMensualDetalle'/>"><i class="icon-list icon-white"></i> <s:message code='informeMensualDetalle.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/colportaje/informes/informeMensualDetalle/actualiza" />
            <form:form commandName="informeMensualDetalle" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:hidden path="informeMensual.id" />

                <fieldset>
                    <s:bind path="informeMensualDetalle.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="128" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.hrsTrabajadas">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="hrsTrabajadas">
                                <s:message code="hrsTrabajadas.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="hrsTrabajadas" maxlength="128" required="true" />
                            <form:errors path="hrsTrabajadas" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.literaturaVendida">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="literaturaVendida">
                                <s:message code="literaturaVendida.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="literaturaVendida" maxlength="128" required="true" />
                            <form:errors path="literaturaVendida" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.totalVentas">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="totalVentas">
                                <s:message code="totalVentas.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="totalVentas" maxlength="128" required="true" />
                            <form:errors path="totalVentas" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.totalPedidos">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="totalPedidos">
                                <s:message code="totalPedidos.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="totalPedidos" maxlength="128" required="true" />
                            <form:errors path="totalPedidos" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.literaturaGratis">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="literaturaGratis">
                                <s:message code="literaturaGratis.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="literaturaGratis" maxlength="128" required="true" />
                            <form:errors path="literaturaGratis" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.oracionesOfrecidas">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="oracionesOfrecidas">
                                <s:message code="oracionesOfrecidas.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="oracionesOfrecidas" maxlength="128" required="true" />
                            <form:errors path="oracionesOfrecidas" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.casasVisitadas">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="casasVisitadas">
                                <s:message code="casasVisitadas.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="casasVisitadas" maxlength="128" required="true" />
                            <form:errors path="casasVisitadas" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.contactosEstudiosBiblicos">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="contactosEstudiosBiblicos">
                                <s:message code="contactosEstudiosBiblicos.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="contactosEstudiosBiblicos" maxlength="128" required="true" />
                            <form:errors path="contactosEstudiosBiblicos" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeMensualDetalle.bautizados">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="bautizados">
                                <s:message code="bautizados.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="bautizados" maxlength="128" required="true" />
                            <form:errors path="bautizados" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportaje/informes/informeMensualDetalle/ver/${informeMensualDetalle.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
            <script>
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option","firstDay",0);
            </script>
        </content>    
    </body>
</html>
