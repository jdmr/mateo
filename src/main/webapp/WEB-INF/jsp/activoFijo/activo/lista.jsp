<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <h1><s:message code="activo.lista.label" /></h1>
        <hr/>
        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/activoFijo/activo' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <div class="well">
                <div class="row-fluid">
                    <a class="btn btn-primary" href="<s:url value='/activoFijo/activo/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='activo.nuevo.label' /></a>
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                    <div class="btn-group" style="display: inline-block; position: absolute; margin-left: 5px;">
                        <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                        <button class="btn dropdown-toggle" data-toggle="dropdown">
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <a id="buscarFechaAnchor" href="#"><s:message code="buscar.fecha.button" /></a>
                            <a id="buscarCuentaAnchor" href="#"><s:message code="buscar.centroCosto.button" /></a>
                            <a id="buscarProveedorAnchor" href="#"><s:message code="buscar.proveedor.button" /></a>
                            <a id="buscarTipoActivoAnchor" href="#"><s:message code="buscar.tipoActivo.button" /></a>
                            <a id="buscarResponsableAnchor" href="#"><s:message code="buscar.responsable.button" /></a>
                            <a id="buscarBajasAnchor" href="#"><s:message code="buscar.bajas.button" /></a>
                            <a id="buscarReubicacionesAnchor" href="#"><s:message code="buscar.reubicaciones.button" /></a>
                        </ul>
                    </div>
                </div>
                <div id="buscarFechaDiv" class="row-fluid" style="<c:if test='${empty param.fechaIniciado and empty param.fechaTerminado}'>display: none; </c:if>margin-top: 10px;">
                    <label>
                        <s:message code="fecha.iniciado" /><br/>
                        <input type="text" name="fechaIniciado" id="fechaIniciado" value="${param.fechaIniciado}" />
                    </label><br/>
                    <label>
                        <s:message code="fecha.terminado" /><br/>
                        <input type="text" name="fechaTerminado" id="fechaTerminado" value="${param.fechaTerminado}" />
                    </label>
                </div>
                <div id="buscarCuentaDiv" class="row-fluid" style="<c:if test='${empty param.cuentaNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="centroCosto.label" /><br/>
                        <input type="hidden" name="cuentaId" id="cuentaId" value="${param.cuentaId}" />
                        <input type="text" name="cuentaNombre" id="cuentaNombre" value="${param.cuentaNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarProveedorDiv" class="row-fluid" style="<c:if test='${empty param.proveedorNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="proveedor.label" /><br/>
                        <input type="hidden" name="proveedorId" id="proveedorId" value="${param.proveedorId}" />
                        <input type="text" name="proveedorNombre" id="proveedorNombre" value="${param.proveedorNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarTipoActivoDiv" class="row-fluid" style="<c:if test='${empty param.tipoActivoNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="tipoActivo.label" /><br/>
                        <input type="hidden" name="tipoActivoId" id="tipoActivoId" value="${param.tipoActivoId}" />
                        <input type="text" name="tipoActivoNombre" id="tipoActivoNombre" value="${param.tipoActivoNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarResponsableDiv" class="row-fluid" style="<c:if test='${empty param.responsableNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="responsable.label" /><br/>
                        <input type="text" name="responsableNombre" id="responsableNombre" value="${param.responsableNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarBajasDiv" class="row-fluid" style="<c:if test='${param.bajas != 1}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <input type="checkbox" name="bajas" id="bajas" value="1" <c:if test="${param.bajas == 1}">checked="checked"</c:if> />
                        <s:message code="buscar.bajas.button" />
                    </label>
                </div>
                <div id="buscarReubicacionesDiv" class="row-fluid" style="<c:if test='${param.reubicaciones != 1}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <input type="checkbox" name="reubicaciones" id="reubicaciones" value="1" <c:if test="${param.reubicaciones == 1}">checked="checked"</c:if> />
                        <s:message code="buscar.reubicaciones.button" />
                    </label>
                </div>
            </div>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <h4><s:message code="activo.resumen.message" arguments="${resumen}" /></h4>
            <table id="lista" class="table table-striped">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="codigo" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="descripcion" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="factura" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="poliza" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="marca" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="serial" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="ubicacion" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="responsable" />
                        </jsp:include>
                        
                        <th><s:message code="proveedor.label" /></th>
                        
                        <th><s:message code="tipoActivo.label" /></th>
                        
                        <th><s:message code="cuenta.label" /></th>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaCompra" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="moi" />
                            <jsp:param name="style" value="text-align:right;" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaDepreciacion" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="depreciacionAnual" />
                            <jsp:param name="style" value="text-align:right;" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="depreciacionMensual" />
                            <jsp:param name="style" value="text-align:right;" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="depreciacionAcumulada" />
                            <jsp:param name="style" value="text-align:right;" />
                        </jsp:include>
                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="valorNeto" />
                            <jsp:param name="style" value="text-align:right;" />
                        </jsp:include>
                        
                        <th><s:message code="empresa.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${activos}" var="activo" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/activoFijo/activo/ver/${activo.id}' />">${activo.folio}</a></td>
                            <td>${activo.codigo}</td>
                            <td>${activo.descripcion}</td>
                            <td>${activo.factura}</td>
                            <td>${activo.poliza}</td>
                            <td>${activo.marca}</td>
                            <td>${activo.serial}</td>
                            <td>${activo.ubicacion}</td>
                            <td>${activo.responsable}</td>
                            <td>${activo.proveedor.nombre}</td>
                            <td>${activo.tipoActivo.nombre}</td>
                            <td>${activo.cuenta.nombre}</td>
                            <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${activo.fechaCompra}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${activo.moi}" type="currency" currencySymbol="$" /></td>
                            <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${activo.fechaDepreciacion}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionAnual}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionMensual}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionAcumulada}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${activo.valorNeto}" type="currency" currencySymbol="$" /></td>
                            <td>${activo.empresa.nombre}</td>
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
                            
                    $("a#buscarFechaAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarFechaDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#fechaIniciado").focus();
                        });
                    });                
                            
                    $("a#buscarCuentaAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarCuentaDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#cuentaNombre").focus();
                        });
                    });                
                            
                    $("a#buscarProveedorAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarProveedorDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#proveedorNombre").focus();
                        });
                    });                
                            
                    $("a#buscarTipoActivoAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarTipoActivoDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#tipoActivoNombre").focus();
                        });
                    });                
                            
                    $("a#buscarResponsableAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarResponsableDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#responsableNombre").focus();
                        });
                    });                
                            
                    $("a#buscarBajasAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarBajasDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#bajas").focus();
                        });
                    });                
                            
                    $("a#buscarReubicacionesAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarReubicacionesDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#reubicaciones").focus();
                        });
                    });                
                });
            </script>
        </content>
    </body>
</html>
