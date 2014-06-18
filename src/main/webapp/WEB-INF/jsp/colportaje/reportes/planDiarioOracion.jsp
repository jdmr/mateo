<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="planDiarioOracion.label" /></title>
    </head>
    <body>
        <jsp:include page="./menu.jsp" >
            <jsp:param name="menu" value="planDiarioOracion" />
        </jsp:include>

        <h1><s:message code="planDiarioOracion.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/reportes/planDiarioOracion' />">
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
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            
            
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th><s:message code="fechaDeNacimiento.label" /></th>
                        <th><s:message code="clave.label" /></th>
                        <th><s:message code="nombre.label" /></th>
                        <th><s:message code="apPaterno.label" /></th>
                        <th><s:message code="apMaterno.label" /></th>
                        <th><s:message code="calle.label" /></th>
                        <th><s:message code="municipio.label" /></th>
                        <th><s:message code="colonia.label" /></th>
                        <th><s:message code="telefono.label" /></th>
                        <th><s:message code="correo.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${planDiarioOracion}" var="clp" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${clp.fechaDeNacimiento}" /></td>
                            <td>${clp.clave}</td>
                            <td>${clp.nombre}</td>
                            <td>${clp.apPaterno}</td>
                            <td>${clp.apMaterno}</td>
                            <td>${clp.calle}</td>
                            <td>${clp.municipio}</td>
                            <td>${clp.colonia}</td>
                            <td>${clp.telefono}</td>
                            <td>${clp.correo}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </form>        
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
