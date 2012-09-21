<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="producto.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="producto" />
        </jsp:include>

        <h1><s:message code="producto.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/inventario/producto' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <div class="well">
                <div class="row-fluid">
                    <a class="btn btn-primary" href="<s:url value='/inventario/producto/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='producto.nuevo.label' /></a>
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                    <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                    <a id="buscarFechaAnchor" class="btn" href="#"><s:message code="buscar.fecha.button" /></a>
                    <input type="checkbox" name="inactivo" id="inactivo" value="1" <c:if test="${not empty param.inactivo}">checked="checked"</c:if> /> <s:message code="buscar.inactivos.check" />
                </div>
                <div id="buscarFechaDiv" class="row-fluid" style="<c:if test='${empty param.fecha}'>display: none; </c:if>margin-top: 10px;">
                    <label>
                        <s:message code="fecha.label" /><br/>
                        <input type="text" name="fecha" id="fecha" value="${param.fecha}" />
                    </label><br/>
                </div>
            </div>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${producto != null}">
                <s:bind path="producto.*">
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
                            <jsp:param name="columna" value="sku" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="nombre" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="descripcion" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="marca" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="modelo" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="ubicacion" />
                        </jsp:include>
                        <th style="text-align:right;">
                            <a href="javascript:ordena('existencia');">
                                <s:message code="existencia.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'existencia' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'existencia' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th style="text-align:right;">
                            <a href="javascript:ordena('precioUnitario');">
                                <s:message code="precioUnitario.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'precioUnitario' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'precioUnitario' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th style="text-align:center;">
                            <a href="javascript:ordena('fraccion');">
                                <s:message code="fraccion.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'fraccion' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'fraccion' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${productos}" var="producto" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/inventario/producto/ver/${producto.id}' />">${producto.sku}</a></td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.marca}</td>
                            <td>${producto.modelo}</td>
                            <td>${producto.ubicacion}</td>
                            <td style="text-align:right;">
                                <c:choose>
                                    <c:when test="${producto.fraccion}">
                                        ${producto.existencia}
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${producto.existencia}" minFractionDigits="0" maxFractionDigits="0" />
                                    </c:otherwise>
                                </c:choose>
                                &nbsp;${producto.unidadMedida}
                            </td>
                            <td style="text-align:right;">${producto.precioUnitario}</td>
                            <td style="text-align:center;"><input type="checkbox" value="" disabled="true" <c:if test="${producto.fraccion}">checked="checked"</c:if> /></td>
                            <td>${producto.almacen.nombre}</td>
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
                    $("input#fecha").datepicker();
                        
                    $("a#buscarFechaAnchor").click(function(e) {
                        e.preventDefault();
                        $("div#buscarFechaDiv").show('slide', {direction:'up'}, 500, function() {
                            $("input#fecha").focus();
                        });
                    });
                });
            </script>
        </content>
    </body>
</html>
