<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="entrada.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="entrada" />
        </jsp:include>

        <h1><s:message code="entrada.lista.label" /></h1>
        <hr/>
        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/inventario/entrada' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <div class="well">
                <div class="row-fluid">
                    <a class="btn btn-primary" href="<s:url value='/inventario/entrada/nueva'/>"><i class="icon-shopping-cart icon-white"></i> <s:message code='entrada.nueva.label' /></a>
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                    <div class="btn-group" style="display: inline-block; position: absolute; margin-left: 5px;">
                        <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                        <button class="btn dropdown-toggle" data-toggle="dropdown">
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a id="buscarFechaAnchor" href="#"><s:message code="buscar.fecha.button" /></a></li>
                            <li><a id="buscarProveedorAnchor" href="#"><s:message code="buscar.proveedor.button" /></a></li>
                            <li><a id="buscarEstatusAnchor" href="#"><s:message code="buscar.estatus.button" /></a></li>
                        </ul>
                    </div>
                </div>
                <div id="buscarFechaDiv" class="row-fluid" style="<c:if test='${empty param.fechaIniciado and empty param.fechaTerminado}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="fecha.iniciado" /><br/>
                        <input type="text" name="fechaIniciado" id="fechaIniciado" value="${param.fechaIniciado}" />
                    </label><br/>
                    <label>
                        <s:message code="fecha.terminado" /><br/>
                        <input type="text" name="fechaTerminado" id="fechaTerminado" value="${param.fechaTerminado}" />
                    </label>
                </div>
                <div id="buscarProveedorDiv" class="row-fluid" style="<c:if test='${empty param.proveedorNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="proveedor.label" /><br/>
                        <input type="hidden" name="proveedorId" id="proveedorId" value="${param.proveedorId}" />
                        <input type="text" name="proveedorNombre" id="proveedorNombre" value="${param.proveedorNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarEstatusDiv" class="row-fluid" style="<c:if test='${not estatus}'>display: none;</c:if> margin-top: 10px;">
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="ABIERTA" id="ABIERTA" <c:if test="${param.ABIERTA == 'on'}">checked="checked"</c:if>/>
                            <s:message code="ABIERTA" />
                        </label>
                    </div>
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="CERRADA" id="CERRADA" <c:if test="${param.CERRADA == 'on'}">checked="checked"</c:if>/>
                            <s:message code="CERRADA" />
                        </label>
                    </div>
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="FACTURADA" id="FACTURADA" <c:if test="${param.FACTURADA == 'on'}">checked="checked"</c:if>/>
                            <s:message code="FACTURADA" />
                        </label>
                    </div>
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="PENDIENTE" id="PENDIENTE" <c:if test="${param.PENDIENTE == 'on'}">checked="checked"</c:if>/>
                            <s:message code="PENDIENTE" />
                        </label>
                    </div>
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="CANCELADA" id="CANCELADA" <c:if test="${param.CANCELADA == 'on'}">checked="checked"</c:if>/>
                            <s:message code="CANCELADA" />
                        </label>
                    </div>
                    <div class="span2">
                        <label class="checkbox">
                            <input type="checkbox" name="DEVOLUCION" id="DEVOLUCION" <c:if test="${param.DEVOLUCION == 'on'}">checked="checked"</c:if>/>
                            <s:message code="DEVOLUCION" />
                        </label>
                    </div>
                </div>
            </div>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${entrada != null}">
                <s:bind path="entrada.*">
                    <c:if test="${not empty status.errorMessages}">
                        <div class="alert alert-block alert-error fade in" role="status">
                            <a class="close" data-dismiss="alert">×</a>
                            <c:forEach var="error" items="${status.errorMessages}">
                                <c:out value="${error}" escapeXml="false"/><br />
                            </c:forEach>
                        </div>
                    </c:if>
                </s:bind>
            </c:if>

            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                            <jsp:param name="style" value="width:150px;" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="factura" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaFactura" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaModificacion" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="estatus" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="proveedor" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="iva" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="total" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="devolucion" />
                        </jsp:include>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${entradas}" var="entrada" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/inventario/entrada/ver/${entrada.id}' />">${entrada.folio}</a></td>
                            <td>${entrada.factura}</td>
                            <td><fmt:formatDate pattern="yyyy/MMM/dd" value="${entrada.fechaFactura}" /></td>
                            <td><fmt:formatDate pattern="yyyy/MMM/dd HH:mm:ss" value="${entrada.fechaModificacion}" /></td>
                            <td><s:message code="${entrada.estatus.nombre}" /></td>
                            <td>${entrada.proveedor.nombre}</td>
                            <td style="text-align:right;"><fmt:formatNumber value="${entrada.iva}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${entrada.total}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:center;"><input type="checkbox" disabled="true" <c:if test="${entrada.devolucion}">checked="checked"</c:if> /></td>
                            <td>${entrada.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
        </form>        
    <content>
        <script src="<c:url value='/js/lista.js' />"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("input#fechaTerminado").datepicker();
                        
                $("input#fechaIniciado").datepicker();
                
                $('input#proveedorNombre').autocomplete({
                    source: "<c:url value='/inventario/entrada/proveedores' />",
                    select: function(event, ui) {
                        $("input#proveedorId").val(ui.item.id);
                        return false;
                    }
                });
                            
                $("a#buscarFechaAnchor").click(function(e) {
                    e.preventDefault();
                    $("div#buscarFechaDiv").show('slide', {direction:'up'}, 500, function() {
                        $("input#fechaIniciado").focus();
                    });
                });
                
                $("a#buscarProveedorAnchor").click(function(e) {
                    e.preventDefault();
                    $("div#buscarProveedorDiv").show('slide', {direction:'up'}, 500, function() {
                        $("input#proveedorNombre").focus();
                    });
                });
                
                $("a#buscarEstatusAnchor").click(function(e) {
                    e.preventDefault();
                    $("div#buscarEstatusDiv").show('slide', {direction:'up'}, 500, function() {
                        $("input#ABIERTA").focus();
                    });
                });
            });
        </script>
    </content>
</body>
</html>
