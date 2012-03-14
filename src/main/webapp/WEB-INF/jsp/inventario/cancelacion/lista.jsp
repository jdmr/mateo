<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cancelacion.lista.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
                <li class="active"><a href="<s:url value='/inventario/cancelacion'/>" ><s:message code="cancelacion.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
            </ul>
        </nav>

        <h1><s:message code="cancelacion.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/inventario/cancelacion' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            
            <table id="lista" class="table table-striped">
                <thead>
                    <tr>
                        <th>
                            <a href="javascript:ordena('folio');">
                                <s:message code="folio.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'folio' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'folio' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th>
                            <a href="javascript:ordena('creador');">
                                <s:message code="creador.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'creador' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'creador' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th>
                            <a href="javascript:ordena('fechaCreacion');">
                                <s:message code="fechaCreacion.label" />
                                <c:choose>
                                    <c:when test="${param.order == 'fechaCreacion' && param.sort == 'asc'}">
                                        <i class="icon-chevron-up"></i>
                                    </c:when>
                                    <c:when test="${param.order == 'fechaCreacion' && param.sort == 'desc'}">
                                        <i class="icon-chevron-down"></i>
                                    </c:when>
                                </c:choose>
                            </a>
                        </th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cancelaciones}" var="cancelacion" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/inventario/cancelacion/ver/${cancelacion.id}' />">${cancelacion.folio}</a></td>
                            <td>${cancelacion.creador}</td>
                            <td>${cancelacion.fechaCreacion}</td>
                            <td>${cancelacion.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="row-fluid">
                <div class="span8">
                    <div class="pagination">
                        <ul>
                            <li class="disabled"><a href="#"><s:message code="mensaje.paginacion" arguments="${paginacion}" /></a></li>
                            <c:forEach items="${paginas}" var="paginaId">
                                <li <c:if test="${pagina == paginaId}" >class="active"</c:if>>
                                    <a href="javascript:buscaPagina(${paginaId});" >${paginaId}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="span4">
                    <div class="btn-group pull-right" style="margin-top: 22px;margin-left: 10px;">
                        <button id="enviaCorreoBtn" class="btn" data-loading-text="<s:message code='enviando.label'/>" onclick="javascript:enviaCorreo('XLS');" ><i class="icon-envelope"></i> <s:message code="envia.correo.label" /></button>
                        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="javascript:enviaCorreo('PDF');"><img src="<c:url value='/images/pdf.gif' />" /></a></li>
                            <li><a href="javascript:enviaCorreo('CSV');"><img src="<c:url value='/images/csv.gif' />" /></a></li>
                            <li><a href="javascript:enviaCorreo('XLS');"><img src="<c:url value='/images/xls.gif' />" /></a></li>
                        </ul>
                    </div>
                    <p class="pull-right" style="margin-top: 20px;">
                        <a href="javascript:imprime('PDF');"><img src="<c:url value='/images/pdf.gif' />" /></a>
                        <a href="javascript:imprime('CSV');"><img src="<c:url value='/images/csv.gif' />" /></a>
                        <a href="javascript:imprime('XLS');"><img src="<c:url value='/images/xls.gif' />" /></a>
                    </p>
                </div>
            </div>
        </form>        
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
