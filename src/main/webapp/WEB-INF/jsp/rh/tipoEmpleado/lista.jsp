<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="tipoEmpleado.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="tipoEmpleado" />
        </jsp:include>

        <h1><s:message code="tipoEmpleado.lista.label" /></h1>
        <hr/>
        
        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/rh/tipoEmpleado' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/tipoEmpleado/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='tipoEmpleado.nuevo.label' /></a>
                <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${tipoEmpleado != null}">
                <s:bind path="tipoEmpleado.*">
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
                            <jsp:param name="columna" value="descripcion" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="prefijo" />
                        </jsp:include>
                        
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tipoEmpleadoList}" var="tipoEmpleado" varStatus="id">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/rh/tipoEmpleado/ver/${tipoEmpleado.id}' />">${tipoEmpleado.descripcion}</a></td>
                            <td>${tipoEmpleado.prefijo}</td>
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
