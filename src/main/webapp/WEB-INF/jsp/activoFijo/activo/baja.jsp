<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.baja.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="nuevo-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.baja.label" /></h1>
            <hr/>

            <c:url var="bajaUrl" value="/activoFijo/activo/baja" />
            <form:form commandName="bajaActivo" action="${bajaUrl}" method="post">
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
                                <h3>${bajaActivo.activo.folio}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="poliza.label" /></h4>
                                <h3>${bajaActivo.activo.poliza}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="codigo.label" /></h4>
                                <h3>${bajaActivo.activo.codigo}</h3>
                            </div>
                        </div>
                        <c:if test="${not empty bajaActivo.activo.pedimento or not empty bajaActivo.activo.moneda or bajaActivo.activo.tipoCambio != null}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="pedimento.label" /></h4>
                                    <h3>${bajaActivo.activo.pedimento}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="moneda.label" /></h4>
                                    <h3>${bajaActivo.activo.moneda}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="tipoCambio.label" /></h4>
                                    <h3>${bajaActivo.activo.tipoCambio}</h3>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty bajaActivo.activo.condicion or not empty bajaActivo.activo.procedencia or not empty bajaActivo.activo.factura}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="condicion.label" /></h4>
                                    <h3>${bajaActivo.activo.condicion}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="procedencia.label" /></h4>
                                    <h3>${bajaActivo.activo.procedencia}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="factura.label" /></h4>
                                    <h3>${bajaActivo.activo.factura}</h3>
                                </div>
                            </div>
                        </c:if>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="descripcion.label" /></h4>
                                <h3>${bajaActivo.activo.descripcion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="marca.label" /></h4>
                                <h3>${bajaActivo.activo.marca}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="modelo.label" /></h4>
                                <h3>${bajaActivo.activo.modelo}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="fechaCompra.label" /></h4>
                                <h3>${bajaActivo.activo.fechaCompra}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaDepreciacion.label" /></h4>
                                <h3>${bajaActivo.activo.fechaDepreciacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorNeto.label" /></h4>
                                <h3>${bajaActivo.activo.valorNeto}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="depreciacionAnual.label" /></h4>
                                <h3>${bajaActivo.activo.depreciacionAnual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionMensual.label" /></h4>
                                <h3>${bajaActivo.activo.depreciacionMensual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionAcumulada.label" /></h4>
                                <h3>${bajaActivo.activo.depreciacionAcumulada}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="moi.label" /></h4>
                                <h3>${bajaActivo.activo.moi}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorRescate.label" /></h4>
                                <h3>${bajaActivo.activo.valorRescate}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="inpc.label" /></h4>
                                <h3>${bajaActivo.activo.inpc}</h3>
                            </div>
                        </div>

                        <c:if test="${not empty bajaActivo.activo.ubicacion or bajaActivo.activo.inactivo or activo.fechaInactivo != null}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="ubicacion.label" /></h4>
                                    <h3>${bajaActivo.activo.ubicacion}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="inactivo.label" /></h4>
                                    <h3><form:checkbox path="activo.inactivo" disabled="true" /></h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="fechaInactivo.label" /></h4>
                                    <h3>${bajaActivo.activo.fechaInactivo}</h3>
                                </div>
                            </div>
                        </c:if>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="serial.label" /></h4>
                                <h3>${bajaActivo.activo.serial}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="tipoActivo.label" /></h4>
                                <h3>${bajaActivo.activo.tipoActivo.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="proveedor.label" /></h4>
                                <h3>${bajaActivo.activo.proveedor.nombre}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="centroCosto.label" /></h4>
                                <h3>${bajaActivo.activo.centroCosto.nombreCompleto}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="empresa.label" /></h4>
                                <h3>${bajaActivo.activo.empresa.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="responsable.label" /></h4>
                                <h3>${bajaActivo.activo.responsable}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="seguro.label" /></h4>
                                <h3><form:checkbox path="activo.seguro" disabled="true" /></h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="garantia.label" /></h4>
                                <h3><form:checkbox path="activo.garantia" disabled="true" /></h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="mesesGarantia.label" /></h4>
                                <h3>${bajaActivo.activo.mesesGarantia}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="motivo.label" /></h4>
                                <h3>${bajaActivo.activo.motivo}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaCreacion.label" /></h4>
                                <h3>${bajaActivo.activo.fechaCreacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaModificacion.label" /></h4>
                                <h3>${bajaActivo.activo.fechaModificacion}</h3>
                            </div>
                        </div>

                    </div>
                    <div class="span3">
                        <c:choose>
                            <c:when test="${tieneImagenes}">
                                <c:forEach items="${bajaActivo.activo.imagenes}" var="imagen">
                                    <p><img src="<c:url value='/imagen/mostrar/${imagen.id}' />" /></p>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p><img src="<c:url value='/imagen/mostrar/0' />" /></p>
                            </c:otherwise>
                        </c:choose>
                        <a class="btn" href="<c:url value='/activoFijo/activo/sube/${bajaActivo.activo.id}'/>"><i class="icon-upload"></i> <s:message code="activo.sube.imagen.button" /></a>
                    </div>
                </div>

                <fieldset class="well">
                    <s:bind path="bajaActivo.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" required="true" maxlength="64" cssClass="span3" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="bajaActivo.motivo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="motivo">
                                <s:message code="motivo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select id="motivo" path="motivo" required="true" cssClass="span3" >
                                <form:options items="${motivos}" />
                            </form:select>
                            <form:errors path="motivo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="bajaActivo.comentarios">
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
                    <a class="btn btn-large" href="<s:url value='/activoFijo/activo/ver/${bajaActivo.activo.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </fieldset>
            </form:form>
        </div>
    <content>
        <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
        <script>
            $(document).ready(function() {
                $('select#motivo').chosen();
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option","firstDay",0);
                $('input#fecha').focus();
            });
        </script>                    
    </content>
</body>
</html>
