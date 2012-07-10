<%-- 
    Document   : sube
    Created on : Jul 9, 2012, 2:56:57 PM
    Author     : J. David Mendoza <jdmendoza@um.edu.mx>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.reubica.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="nuevo-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.reubica.label" /></h1>
            <hr/>

            <c:url var="reubicaUrl" value="/activoFijo/activo/reubica" />
            <form:form commandName="reubicacion" action="${reubicaUrl}" method="post">
                <form:hidden path="activo.id" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <div class="row-fluid">
                    <div class="span9">
                        <div class="row-fluid">
                            <div class="span4">
                                <h4><s:message code="folio.label" /></h4>
                                <h3>${reubicacion.activo.folio}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="poliza.label" /></h4>
                                <h3>${reubicacion.activo.poliza}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="codigo.label" /></h4>
                                <h3>${reubicacion.activo.codigo}</h3>
                            </div>
                        </div>
                        <c:if test="${not empty reubicacion.activo.pedimento or not empty reubicacion.activo.moneda or reubicacion.activo.tipoCambio != null}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="pedimento.label" /></h4>
                                    <h3>${reubicacion.activo.pedimento}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="moneda.label" /></h4>
                                    <h3>${reubicacion.activo.moneda}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="tipoCambio.label" /></h4>
                                    <h3>${reubicacion.activo.tipoCambio}</h3>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty reubicacion.activo.condicion or not empty reubicacion.activo.procedencia or not empty reubicacion.activo.factura}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="condicion.label" /></h4>
                                    <h3>${reubicacion.activo.condicion}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="procedencia.label" /></h4>
                                    <h3>${reubicacion.activo.procedencia}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="factura.label" /></h4>
                                    <h3>${reubicacion.activo.factura}</h3>
                                </div>
                            </div>
                        </c:if>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="descripcion.label" /></h4>
                                <h3>${reubicacion.activo.descripcion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="marca.label" /></h4>
                                <h3>${reubicacion.activo.marca}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="modelo.label" /></h4>
                                <h3>${reubicacion.activo.modelo}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="fechaCompra.label" /></h4>
                                <h3>${reubicacion.activo.fechaCompra}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaDepreciacion.label" /></h4>
                                <h3>${reubicacion.activo.fechaDepreciacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorNeto.label" /></h4>
                                <h3>${reubicacion.activo.valorNeto}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="depreciacionAnual.label" /></h4>
                                <h3>${reubicacion.activo.depreciacionAnual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionMensual.label" /></h4>
                                <h3>${reubicacion.activo.depreciacionMensual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionAcumulada.label" /></h4>
                                <h3>${reubicacion.activo.depreciacionAcumulada}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="moi.label" /></h4>
                                <h3>${reubicacion.activo.moi}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorRescate.label" /></h4>
                                <h3>${reubicacion.activo.valorRescate}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="inpc.label" /></h4>
                                <h3>${reubicacion.activo.inpc}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="ubicacion.label" /></h4>
                                <h3>${reubicacion.activo.ubicacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="inactivo.label" /></h4>
                                <h3>${reubicacion.activo.inactivo}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaInactivo.label" /></h4>
                                <h3>${activo.fechaInactivo}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="serial.label" /></h4>
                                <h3>${reubicacion.activo.serial}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="tipoActivo.label" /></h4>
                                <h3>${reubicacion.activo.tipoActivo.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="proveedor.label" /></h4>
                                <h3>${reubicacion.activo.proveedor.nombre}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="cuenta.label" /></h4>
                                <h3>${reubicacion.activo.cuenta.nombreCompleto}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="empresa.label" /></h4>
                                <h3>${reubicacion.activo.empresa.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="responsable.label" /></h4>
                                <h3>${reubicacion.activo.responsable}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="seguro.label" /></h4>
                                <h3>${reubicacion.activo.seguro}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="garantia.label" /></h4>
                                <h3>${reubicacion.activo.garantia}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="mesesGarantia.label" /></h4>
                                <h3>${reubicacion.activo.mesesGarantia}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="motivo.label" /></h4>
                                <h3>${reubicacion.activo.motivo}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaCreacion.label" /></h4>
                                <h3>${reubicacion.activo.fechaCreacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaModificacion.label" /></h4>
                                <h3>${reubicacion.activo.fechaModificacion}</h3>
                            </div>
                        </div>

                    </div>
                    <div class="span3">
                        <c:forEach items="${reubicacion.activo.imagenes}" var="imagen">
                            <p><img src="<c:url value='/imagen/mostrar/${imagen.id}' />" /></p>
                            </c:forEach>
                    </div>
                </div>

                <fieldset class="well">
                    <s:bind path="reubicacion.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" required="true" maxlength="64" cssClass="span3" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="reubicacion.comentarios">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="comentarios">
                                <s:message code="comentarios.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:textarea id="comentarios" path="comentarios" required="true" cssClass="span6" cssStyle="height: 100px;" />
                            <form:errors path="comentarios" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <button type="submit" name="actualizarBtn" class="btn btn-danger btn-large" id="actualizar" onclick="return confirm('<s:message code="confirma.baja.message" />');" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/activoFijo/activo/ver/${reubicacion.activo.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </fieldset>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option","firstDay",0);
                $("input#fecha").focus();
            });
        </script>                    
    </content>
</body>
</html>
