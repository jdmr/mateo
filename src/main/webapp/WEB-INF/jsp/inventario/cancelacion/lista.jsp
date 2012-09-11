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
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="cancelacion" />
        </jsp:include>

        <h1><s:message code="cancelacion.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/inventario/cancelacion' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <div class="well">
                <div class="row-fluid">
                    <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                    <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
                    <a id="buscarFechaAnchor" class="btn" href="#"><s:message code="buscar.fecha.button" /></a>
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
            </div>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                            <jsp:param name="style" value="width:150px;" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="creador" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaCreacion" />
                        </jsp:include>
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
                });
            </script>
        </content>
    </body>
</html>
