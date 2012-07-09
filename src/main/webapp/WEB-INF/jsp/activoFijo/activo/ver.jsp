<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="ver-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/activoFijo/activo'/>"><i class="icon-list icon-white"></i> <s:message code='activo.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/activoFijo/activo/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='activo.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                            <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/activoFijo/activo/preparaBaja" />
            <form:form commandName="activo" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid">
                    <div class="span9">
                        <div class="row-fluid">
                            <div class="span4">
                                <h4><s:message code="folio.label" /></h4>
                                <h3>${activo.folio}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="poliza.label" /></h4>
                                <h3>${activo.poliza}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="codigo.label" /></h4>
                                <h3>${activo.codigo}</h3>
                            </div>
                        </div>
                        <c:if test="${not empty activo.pedimento or not empty activo.moneda or activo.tipoCambio != null}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="pedimento.label" /></h4>
                                    <h3>${activo.pedimento}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="moneda.label" /></h4>
                                    <h3>${activo.moneda}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="tipoCambio.label" /></h4>
                                    <h3>${activo.tipoCambio}</h3>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty activo.condicion or not empty activo.procedencia or not empty activo.factura}">
                            <div class="row-fluid" style="margin-top: 10px;">
                                <div class="span4">
                                    <h4><s:message code="condicion.label" /></h4>
                                    <h3>${activo.condicion}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="procedencia.label" /></h4>
                                    <h3>${activo.procedencia}</h3>
                                </div>
                                <div class="span4">
                                    <h4><s:message code="factura.label" /></h4>
                                    <h3>${activo.factura}</h3>
                                </div>
                            </div>
                        </c:if>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="descripcion.label" /></h4>
                                <h3>${activo.descripcion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="marca.label" /></h4>
                                <h3>${activo.marca}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="modelo.label" /></h4>
                                <h3>${activo.modelo}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="fechaCompra.label" /></h4>
                                <h3>${activo.fechaCompra}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaDepreciacion.label" /></h4>
                                <h3>${activo.fechaDepreciacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorNeto.label" /></h4>
                                <h3>${activo.valorNeto}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="depreciacionAnual.label" /></h4>
                                <h3>${activo.depreciacionAnual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionMensual.label" /></h4>
                                <h3>${activo.depreciacionMensual}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="depreciacionAcumulada.label" /></h4>
                                <h3>${activo.depreciacionAcumulada}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="moi.label" /></h4>
                                <h3>${activo.moi}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="valorRescate.label" /></h4>
                                <h3>${activo.valorRescate}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="inpc.label" /></h4>
                                <h3>${activo.inpc}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="ubicacion.label" /></h4>
                                <h3>${activo.ubicacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="inactivo.label" /></h4>
                                <h3><form:checkbox path="inactivo" disabled="true" /></h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaInactivo.label" /></h4>
                                <h3>${activo.fechaInactivo}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="serial.label" /></h4>
                                <h3>${activo.serial}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="tipoActivo.label" /></h4>
                                <h3>${activo.tipoActivo.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="proveedor.label" /></h4>
                                <h3>${activo.proveedor.nombre}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="cuenta.label" /></h4>
                                <h3>${activo.cuenta.nombreCompleto}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="empresa.label" /></h4>
                                <h3>${activo.empresa.nombre}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="responsable.label" /></h4>
                                <h3>${activo.responsable}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="seguro.label" /></h4>
                                <h3><form:checkbox path="seguro" disabled="true" /></h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="garantia.label" /></h4>
                                <h3><form:checkbox path="garantia" disabled="true" /></h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="mesesGarantia.label" /></h4>
                                <h3>${activo.mesesGarantia}</h3>
                            </div>
                        </div>

                        <div class="row-fluid" style="margin-top: 10px;">
                            <div class="span4">
                                <h4><s:message code="motivo.label" /></h4>
                                <h3>${activo.motivo}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaCreacion.label" /></h4>
                                <h3>${activo.fechaCreacion}</h3>
                            </div>
                            <div class="span4">
                                <h4><s:message code="fechaModificacion.label" /></h4>
                                <h3>${activo.fechaModificacion}</h3>
                            </div>
                        </div>

                    </div>
                    <div class="span3">
                        <c:choose>
                            <c:when test="${tieneImagenes}">
                                <c:forEach items="${activo.imagenes}" var="imagen">
                                    <p><img src="<c:url value='/imagen/mostrar/${imagen.id}' />" /></p>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p><img src="<c:url value='/imagen/mostrar/0' />" /></p>
                            </c:otherwise>
                        </c:choose>
                        <a class="btn" href="<c:url value='/activoFijo/activo/sube/${activo.id}'/>"><i class="icon-upload"></i> <s:message code="activo.sube.imagen.button" /></a>
                    </div>
                </div>

                <p class="well" style="margin-top: 10px;">
                    <a href="<c:url value='/activoFijo/activo/edita/${activo.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='baja.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
