<%-- 
    Document   : nuevo
    Created on : Jan 27, 2012, 10:37:52 AM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="proveedorFacturas.nuevo.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="proveedorFacturas" />
        </jsp:include>

        <div id="nuevo-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="proveedorFacturas.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/proveedorFacturas'/>"><i class="icon-list icon-white"></i> <s:message code='proveedorFacturas.lista.label' /></a>
            </p>
            <form:form commandName="proveedorFacturas" action="graba" method="post">
                <input type="hidden" name="enviaCorreo" value="${enviaCorreo}" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="proveedorFacturas.apPaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="apPaterno">
                                <s:message code="apPaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apPaterno" maxlength="128" required="true" />
                            <form:errors path="apPaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.apMaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="apMaterno">
                                <s:message code="apMaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apMaterno" maxlength="128" required="true" />
                            <form:errors path="apMaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.username">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="username">
                                <s:message code="username.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="username" maxlength="128" required="true" />
                            <form:errors path="username" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>


                    <s:bind path="proveedorFacturas.correo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="correo">
                                <s:message code="correo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="correo" required="true"  type="email" />
                            <form:errors path="correo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.razonSocial">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="razonSocial">
                                <s:message code="razonSocial.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="razonSocial" required="true" maxlength="128" />
                            <form:errors path="razonSocial" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.rfc">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="rfc">
                                <s:message code="rfc.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="rfc" required="true" maxlength="128" />
                            <form:errors path="rfc" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.idFiscal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="idFiscal">
                                <s:message code="idFiscal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="idFiscal" required="true" maxlength="128" />
                            <form:errors path="idFiscal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.CURP">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="CURP">
                                <s:message code="curp.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="CURP" required="true" maxlength="128" />
                            <form:errors path="CURP" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.direccion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="direccion">
                                <s:message code="direccion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="direccion" required="true" maxlength="250" />
                            <form:errors path="direccion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.telefono">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="telefono">
                                <s:message code="telefono.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="telefono" required="true" maxlength="15"  />
                            <form:errors path="telefono" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.tipoTercero">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoTercero">
                                <s:message code="tipoTercero.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="tipoTercero" required="true" maxlength="128"  />
                            <form:errors path="tipoTercero" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.clabe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clabe">
                                <s:message code="clabe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clabe" required="true" maxlength="128"  />
                            <form:errors path="clabe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <%--
                    <s:bind path="proveedorFacturas.banco">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="banco">
                                <s:message code="banco.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="banco" required="true" maxlength="128"  />
                            <form:errors path="banco" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    --%>
                    <s:bind path="proveedorFacturas.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" required="true" maxlength="128"  />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.password">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="login.password" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="password" required="true" maxlength="128"  />
                            <form:errors path="password" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="proveedorFacturas.cuentaCheque">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="cuentaCheque">
                                <s:message code="cuentaCheque.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="cuentaCheque" required="true" maxlength="128"  />
                            <form:errors path="cuentaCheque" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/factura/proveedorFacturas'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
        <script>
            $(document).ready(function() {
                $('input#username').focus();
            });
        </script>                    
    </content>
</body>
</html>
