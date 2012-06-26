<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="salida.lista.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="salida" />
        </jsp:include>

        <h1><s:message code="salida.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/inventario/salida' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <div class="well">
                <div class="row-fluid">
                    <a class="btn btn-primary" href="<s:url value='/inventario/salida/nueva'/>"><i class="icon-shopping-cart icon-white"></i> <s:message code='salida.nueva.label' /></a>
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                    <div class="btn-group" style="display: inline-block; position: absolute; margin-left: 5px;">
                        <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                        <button class="btn dropdown-toggle" data-toggle="dropdown">
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <a id="buscarFechaAnchor" href="#"><s:message code="buscar.fecha.button" /></a>
                            <a id="buscarClienteAnchor" href="#"><s:message code="buscar.cliente.button" /></a>
                            <a id="buscarEstatusAnchor" href="#"><s:message code="buscar.estatus.button" /></a>
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
                <div id="buscarClienteDiv" class="row-fluid" style="<c:if test='${empty param.clienteNombre}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="cliente.label" /><br/>
                        <input type="hidden" name="clienteId" id="clienteId" value="${param.clienteId}" />
                        <input type="text" name="clienteNombre" id="clienteNombre" value="${param.clienteNombre}" class="input-xxlarge" />
                    </label>
                </div>
                <div id="buscarEstatusDiv" class="row-fluid" style="<c:if test='${empty param.estatusId}'>display: none;</c:if> margin-top: 10px;">
                    <label>
                        <s:message code="estatus.label" /><br/>
                        <select name="estatusId" id="estatusId">
                            <option value=""><s:message code="estatus.elija.message" /></option>
                            <c:forEach items="${estados}" var="estatus">
                                <option value="${estatus.id}" <c:if test="${param.estatusId == estatus.id}">selected="selected"</c:if>><s:message code="${estatus.nombre}" /></option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
            </div>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                            <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${salida != null}">
                <s:bind path="salida.*">
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

            <table id="lista" class="table table-striped">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                            <jsp:param name="style" value="width:150px;" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaModificacion" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="estatus" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="reporte" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="atendio" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="empleado" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="departamento" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="cliente" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="iva" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="total" />
                        </jsp:include>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${salidas}" var="salida" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/inventario/salida/ver/${salida.id}' />">${salida.folio}</a></td>
                            <td><fmt:formatDate pattern="yyyy/MMM/dd HH:mm:ss" value="${salida.fechaModificacion}" /></td>
                            <td><s:message code="${salida.estatus.nombre}" /></td>
                            <td>${salida.reporte}</td>
                            <td>${salida.atendio}</td>
                            <td>${salida.empleado}</td>
                            <td>${salida.departamento}</td>
                            <td>${salida.cliente.nombre}</td>
                            <td style="text-align:right;"><fmt:formatNumber value="${salida.iva}" type="currency" currencySymbol="$" /></td>
                            <td style="text-align:right;"><fmt:formatNumber value="${salida.total}" type="currency" currencySymbol="$" /></td>
                            <td>${salida.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
        </form>        
        <content>
            <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
            <script src="<c:url value='/js/lista.js' />"></script>
            <script type="text/javascript">
                $(document).ready(function() {
                    $("input#fechaTerminado").datepicker();
                        
                    $("input#fechaIniciado").datepicker();
                            
                    $('input#clienteNombre').autocomplete({
                        source: "<c:url value='/inventario/salida/clientes' />",
                        select: function(event, ui) {
                            $("input#clienteId").val(ui.item.id);
                            return false;
                        }
                    });
                            
                    $("a#buscarFechaAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarFechaDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#fechaIniciado").focus();
                        });
                    });
                
                    $("a#buscarClienteAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarClienteDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#clienteNombre").focus();
                        });
                    });
                    
                    $("select#estatusId").chosen();
                    
                    $("a#buscarEstatusAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarEstatusDiv").show('slide', {direction:'up'}, 500, function() {
                            $("select#estatusId").focus();
                        });
                    });
                });
            </script>
        </content>
    </body>
</html>
