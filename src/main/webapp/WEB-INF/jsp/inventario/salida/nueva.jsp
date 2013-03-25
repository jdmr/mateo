<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="salida.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="salida" />
        </jsp:include>

        <div id="edita-salida" class="content scaffold-list" role="main">
            <h1><s:message code="salida.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/salida'/>"><i class="icon-list icon-white"></i> <s:message code='salida.lista.label' /></a>
            </p>
            <form:form commandName="salida" action="crea" method="post">
                <form:hidden path="cliente.id" id="clienteId" />
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
                        <div class="span12">
                            <s:bind path="salida.cliente">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="clienteNombre">
                                        <s:message code="cliente.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input id="clienteNombre" path="cliente.nombre" cssClass="span6" />
                                    <form:errors path="cliente" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="salida.reporte">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="reporte">
                                        <s:message code="reporte.label" />
                                    </label>
                                    <form:input path="reporte" cssClass="span6" />
                                    <form:errors path="reporte" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="salida.empleado">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="empleado">
                                        <s:message code="empleado.label" />
                                    </label>
                                    <form:input path="empleado" maxlength="64" cssClass="span6" />
                                    <form:errors path="empleado" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="salida.departamento">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="departamento">
                                        <s:message code="departamento.label" />
                                    </label>
                                    <form:input path="departamento" maxlength="64" cssClass="span6" />
                                    <form:errors path="departamento" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="salida.comentarios">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="comentarios">
                                        <s:message code="comentarios.label" />
                                    </label>
                                    <form:textarea path="comentarios" cssClass="span6" />
                                    <form:errors path="comentarios" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inventario/salida'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#clienteNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/salida/clientes' />",
                            select: function(event, ui) {
                                $("input#clienteId").val(ui.item.id);
                                $("input#reporte").focus();
                                return false;
                            }
                        })
                        .focus();
                });
            </script>                    
        </content>
    </body>
</html>
