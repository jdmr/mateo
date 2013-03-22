<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="facturaAlmacen.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="factura" />
        </jsp:include>

        <div id="edita-factura" class="content scaffold-list" role="main">
            <h1><s:message code="facturaAlmacen.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/factura'/>"><i class="icon-list icon-white"></i> <s:message code='facturaAlmacen.lista.label' /></a>
            </p>
            <c:url var="creaUrl" value='/inventario/factura/crea' />
            <form:form commandName="factura" action="${creaUrl}" method="post">
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
                            <s:bind path="factura.cliente">
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
                            <s:bind path="factura.fecha">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="fecha">
                                        <s:message code="fecha.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="fecha" required="true" maxlength="64" cssClass="span6" />
                                    <form:errors path="fecha" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="factura.comentarios">
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
                    <a class="btn btn-large" href="<s:url value='/inventario/factura'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#clienteNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/factura/clientes' />",
                            select: function(event, ui) {
                                $("input#clienteId").val(ui.item.id);
                                $("#fecha").focus();
                                return false;
                            }
                        })
                        .focus();
                        
                    $("input#fecha").datepicker($.datepicker.regional['es']);
                    $("input#fecha").datepicker("option","firstDay",0);
                });
            </script>                    
        </content>
    </body>
</html>
