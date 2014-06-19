<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="importarDatos.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="importarDatos" />
        </jsp:include>

        <h1><s:message code="importarDatos.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/importarDatos/cargandoArchivo' />">
            
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            
            <p class="well">
                <input type="radio" name="tipoArchivo" value="IG"> Bolet&iacute;n
                <input type="radio" name="tipoArchivo" value="ID">Diezmos
                <input type="radio" name="tipoArchivo" value="INF">INFORMES
            </p>

            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="name" />
                        </jsp:include>                  
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${filesList}" var="iDatos" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><c:out value="${iDatos.name}"/></td>
                            <td><input type="checkbox" name='chk-<c:out value="${iDatos.id}"/>'></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
            <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/admin/empresa'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
        </form>        
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
