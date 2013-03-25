<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cliente.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="cliente" />
        </jsp:include>

        <div id="nuevo-cliente" class="content scaffold-list" role="main">
            <h1><s:message code="cliente.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/cliente'/>"><i class="icon-list icon-white"></i> <s:message code='cliente.lista.label' /></a>
            </p>
            <form:form commandName="cliente" action="crea" method="post">
                <form:hidden path="base" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="cliente.nombre">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="nombre">
                                        <s:message code="nombre.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="nombre" maxlength="128" required="true" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="cliente.nombreCompleto">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="nombreCompleto">
                                        <s:message code="nombreCompleto.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="nombreCompleto" maxlength="128" required="true" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="cliente.rfc">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="rfc">
                                        <s:message code="rfc.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="rfc" maxlength="13" required="true" cssClass="span4" min="12" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="cliente.curp">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="curp">
                                        <s:message code="curp.label" />
                                    </label>
                                    <form:input path="curp" maxlength="18" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span8">
                            <s:bind path="cliente.direccion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="direccion">
                                        <s:message code="direccion.label" />
                                    </label>
                                    <form:textarea path="direccion" cssClass="span8" cssStyle="height: 90px;" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="cliente.telefono">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="telefono">
                                        <s:message code="telefono.label" />
                                    </label>
                                    <form:input path="telefono" maxlength="25" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="cliente.fax">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="fax">
                                        <s:message code="fax.label" />
                                    </label>
                                    <form:input path="fax" maxlength="25" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="cliente.contacto">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="contacto">
                                        <s:message code="contacto.label" />
                                    </label>
                                    <form:input path="contacto" maxlength="64" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="cliente.correo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="correo">
                                        <s:message code="correo.label" />
                                    </label>
                                    <form:input path="correo" maxlength="128" cssClass="span4" type="email" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="cliente.tipoCliente">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoCliente.id">
                                        <s:message code="tipoCliente.label" />
                                    </label>
                                    <form:select id="tipoClienteId" path="tipoCliente.id" items="${tiposDeCliente}" itemLabel="nombre" itemValue="id" cssClass="span4" />
                                    <form:errors path="tipoCliente" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/admin/cliente'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#nombre').focus();
                });
            </script>                    
        </content>
    </body>
</html>
