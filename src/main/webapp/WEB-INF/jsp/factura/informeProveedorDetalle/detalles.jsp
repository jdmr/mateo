<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="autorizarFacturas.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="revisaProveedor" />
        </jsp:include>




        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="autorizarFacturas.label" /></h1>
            <form name="filtraLista" class="form-search" method="post" action="<c:url value='/factura/informeProveedorDetalle/revisar' />">
                <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
                <input type="hidden" name="tipo" id="tipo" value="" />
                <input type="hidden" name="correo" id="correo" value="" />
                <input type="hidden" name="order" id="order" value="${param.order}" />
                <input type="hidden" name="sort" id="sort" value="${param.sort}" />
                <p class="well">
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">

                    <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                </p> </form>
                <form:form commandName="informeProveedorDetalle" action="autorizar" method="get" >
                    <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <div class="row-fluid" style="padding-bottom: 10px;">

                    <table id="lista" class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="autorizar" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="folio" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="proveedor" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="rfc" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="iva" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="subtotal" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="total" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="status" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="fechaFactura" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="informeProveedor" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="xml" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="pdf" />
                                </jsp:include>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${informesProveedorDetalle}" var="informeProveedorDetalle" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td ><input type="checkbox" value="<c:out value="${informeProveedorDetalle.id}"/>" name="checkFacturasid-<c:out value="${informeProveedorDetalle.id}"/>" id="checkFacturasid" /></td>
                                    <td><a href="<c:url value='/factura/informeProveedorDetalle/ver/${informeProveedorDetalle.id}' />">${informeProveedorDetalle.folioFactura}</a></td>                            
                                    <td>${informeProveedorDetalle.nombreProveedor}</td>
                                    <td>${informeProveedorDetalle.RFCProveedor}</td>
                                    <td>${informeProveedorDetalle.IVA}</td>
                                    <td>${informeProveedorDetalle.subtotal}</td>
                                    <td>${informeProveedorDetalle.total}</td>
                                    <td>${informeProveedorDetalle.statusTexto}</td>
                                    <td>${informeProveedorDetalle.fechaFactura}</td>
                                    <td>${informeProveedorDetalle.informeProveedor.id}</td>
                                    <td ><input type="checkbox" disabled="true"  <c:if test="${informeProveedorDetalle.pathXMl!= null && !informeProveedorDetalle.pathXMl.isEmpty()}">checked="checked"</c:if> /></td>
                                    <td ><input type="checkbox" disabled="true"  <c:if test="${informeProveedorDetalle.pathPDF!= null && !informeProveedorDetalle.pathPDF.isEmpty()}">checked="checked"</c:if> /></td>
                                    <td><a href="<c:url value='/factura/informeProveedorDetalle/downloadPdfFile/${informeProveedorDetalle.id}' />">PDF</td>                            
                                    <td><a href="<c:url value='/factura/informeProveedorDetalle/downloadXmlFile/${informeProveedorDetalle.id}' />">XML</a></td>                            
                                </tr>
                            </c:forEach> 
                        </tbody>
                    </table>

                    <p class="well" style="margin-top: 10px;">
                        <button type="submit" name="botonAutorizar" class="btn btn-primary btn-large" id="botonAutorizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='autorizar.label'/></button>
                        <button type="submit" name="botonRechazar" class="btn btn-primary btn-large" id="botonRechazar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='rechazar.label'/></button>
                    </p>
                </form:form>
            </div>
            <content>
                <%--
                <script
        src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script> --%>
                <script>
                    $(document).ready(function() {
                        $('input#nombre').focus();
                        $("input#fechaFactura").datepicker($.datepicker.regional['es']);
                        $("input#fechaFactura").datepicker("option", "firstDay", 0);
                    });

                    $(document).ready(function() {
                        //add more file components if Add is clicked
                        $('#addFile').click(function() {
                            var fileIndex = $('#fileTable tr').children().length - 1;
                            $('#fileTable').append(
                                    '<tr><td>' +
                                    '   <input type="file" name="files[' + fileIndex + ']" />' +
                                    '</td></tr>');
                        });

                    });
                </script>

            </content>
    </body>
</html>
