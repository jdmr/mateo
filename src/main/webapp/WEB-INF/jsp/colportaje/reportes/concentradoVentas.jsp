<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="concentradoVentas.label" /></title>
    </head>
    <body>
        <jsp:include page="./menu.jsp" >
            <jsp:param name="menu" value="concentradoVentas" />
        </jsp:include>

        <h1><s:message code="concentradoVentas.label" /></h1>
        <hr/>
            <h5>                
                 <table>
                    <tr>
                        <td>
                            <span class="label label-success">
                            <s:message code="totalesBoletin.label"/>:<fmt:formatNumber type="currency" currencySymbol="$" value="${totalesBoletin}" />
                            </span>
                        </td>
                        <td>
                            <c:if test="${totalesDiezmo < (totalesBoletin * 0.1)*0.8}">
                                <span class="label label-important">
                            </c:if>
                            <c:if test="${totalesDiezmo >= totalesBoletin * 0.1}">
                                <span class="label label-success">
                            </c:if>
                            <c:if test="${totalesDiezmo > (totalesBoletin * 0.1)*0.8}">
                                <span class="label label-warning">
                            </c:if>
                            <s:message code="totalesDiezmo.label"/>:<fmt:formatNumber type="currency" currencySymbol="$" value="${totalesDiezmo}" />
                            </span>
                        </td>
                    </tr>
                </table>
            </h5>
            
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
           
            <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/reportes/concentradoVentas' />">
            <p class="well">
            <label for="temporada">
            <s:message code="temporada.label" />
            <select name="temporadaId" id="temporadaId" class="input-large search-query" >
                <c:forEach items="${temporadas}" var="tmp">
                    <c:if test="${tmp.id eq temporada.id}">
                        <option value="${tmp.id}" selected="true">${tmp.nombre}</option>
                        
                    </c:if>
                    <c:if test="${not(tmp.id eq temporada.id)}">
                        <option value="${tmp.id}">${tmp.nombre}</option>
                        
                    </c:if>
                </c:forEach>
            </select>
            <button type="submit" class="btn"><s:message code="buscar.label" /></button>
            </p>
            </form>
            
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th><s:message code="colportor.clave.label"/></th>
                        <th><s:message code="nombre.label"/></th>
                        <th><s:message code="boletin.label"/></th>
                        <th><s:message code="diezmo"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${concentradoVentas}" var="clp" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>
                                <c:url var="linky" value="/colportaje/documento/lista">
                                    <c:param name="clave" value="${clp.colportor.clave}"/>
                                    <c:param name="temporadaId" value="${clp.temporadaColportor.temporada.id}"/>
                                </c:url>
                                <a alt="Documentos del Colportor" target="_blank"href='<c:out value="${linky}"/>'>${clp.colportor.clave}</a>
                            </td>
                            <td>${clp.colportor.nombreCompleto}</td>
                            <td><fmt:formatNumber type="currency" currencySymbol="$" value="${clp.acumuladoBoletin}" /></td>
                            <td><fmt:formatNumber type="currency" currencySymbol="$" value="${clp.acumuladoDiezmo}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
