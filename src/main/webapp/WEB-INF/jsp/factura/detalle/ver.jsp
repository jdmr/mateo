<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="detalle.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="detalle" />
        </jsp:include>

        <div id="ver-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="detalle.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/detalle'/>"><i class="icon-list icon-white"></i> <s:message code='detalle.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/factura/detalle/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='detalle.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/factura/detalle/elimina" />
            <form:form commandName="detalle" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="folio.label" /></div>
                    <div class="span11">${detalle.folioFactura}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="proveedor.label" /></div>
                    <div class="span11">${detalle.nombreProveedor}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="rfc.label" /></div>
                    <div class="span11">${detalle.RFCProveedor}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="iva.label" /></div>
                    <div class="span11">${detalle.IVA}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="subtotal.label" /></div>
                    <div class="span11">${detalle.subtotal}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="total.label" /></div>
                    <div class="span11">${detalle.total}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="status.label" /></div>
                    <div class="span11">${detalle.status}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaFactura.label" /></div>
                    <div class="span11">${detalle.fechaFactura}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="informeEmpleado.label" /></div>
                    <div class="span11">${detalle.informeEmpleado.id}</div>
                </div>

                <a  href="<s:url value='/factura/detalle/descargarPdf/${detalle.id}'/>"><img src="/mateo/images/pdf.png" width="120" height="100" /></a>
                <a  href="<s:url value='/factura/detalle/descargarXML/${detalle.id}'/>"><img src="/mateo/images/xml.png" width="120" height="100" /></a>

                <p class="well">
                    <a href="<c:url value='/factura/detalle/edita/${detalle.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>

