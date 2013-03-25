<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="lote.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="salida" />
        </jsp:include>

        <div id="nuevo-salida" class="content scaffold-list" role="main">
            <h1><s:message code="lote.nuevo.label" /></h1>
            <hr/>
            
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-error fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="creaUrl" value="/inventario/salida/lote/crea" />
            <form:form commandName="lote" action="${creaUrl}" method="post">
                <form:hidden path="salida.id" id="salidaId" />
                <form:hidden path="producto.id" id="productoId" />
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
                            <s:bind path="lote.producto">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="productoNombre">
                                        <s:message code="producto.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input id="productoNombre" path="producto.nombre" cssClass="span12" />
                                    <form:errors path="producto" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="lote.cantidad">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="cantidad">
                                        <s:message code="cantidad.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="cantidad" cssClass="span6" required="true" cssStyle="text-align:right;" type="number" step="0.001" min="0" />
                                    <form:errors path="cantidad" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inventario/salida/ver/${lote.salida.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#productoNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/salida/productos' />",
                            select: function(event, ui) {
                                $("input#productoId").val(ui.item.id);
                                $("input#cantidad").focus();
                                return false;
                            }
                        })
                        .focus();
                });
            </script>                    
        </content>
    </body>
</html>
