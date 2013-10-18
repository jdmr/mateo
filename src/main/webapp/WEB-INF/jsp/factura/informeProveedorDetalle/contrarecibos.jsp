<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="contrarecibo.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedorDetalle" />
        </jsp:include>

        <h1><s:message code="contrarecibo.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/factura/informeProveedorDetalle/contrarecibos' />">
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
            <c:if test="${contrarecibos != null}">
                <s:bind path="contrarecibos.*">
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
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="proveedor" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaPago" />
                        </jsp:include>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${contrarecibos}" var="contrarecibo" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>${contrarecibo.id}</td>                            
                            <td>${contrarecibo.proveedorFacturas.nombre}</td>                            
                            <td>${contrarecibo.fechaPago}</td>                            
                            <td><span class="label label-info" <a href="<c:url value='/factura/informeProveedorDetalle/verContrarecibo/${contrarecibo.id}' />">Detalles</a></span></td>
                            <td><span class="label label-info" <a href="<c:url value='/factura/informeProveedorDetalle/cambiarFecha/${contrarecibo.id}' />">Fecha</a></span></td>
                            <td><span class="label label-info" <a href="<c:url value='/factura/informeProveedorDetalle/reporteContrarecibo/${contrarecibo.id}' />">Reporte</a></span></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
        </form>        
    <content>
        <script src="<c:url value='/js/lista.js' />"></script>
    </content>
</body>
</html>
