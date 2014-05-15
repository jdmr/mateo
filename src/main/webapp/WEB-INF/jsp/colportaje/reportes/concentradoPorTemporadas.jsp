<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="concentradoPorTemporadas.label" /></title>
    </head>
    <body>
        <jsp:include page="./menu.jsp" >
            <jsp:param name="menu" value="concentradoPorTemporadas" />
        </jsp:include>

        <h1><s:message code="concentradoPorTemporadas.label" /></h1>
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
                            <c:if test="${totalesDiezmo > (totalesBoletin * 0.1)*0.8 and totalesDiezmo < totalesBoletin * 0.1}">
                                <span class="label label-warning">
                            </c:if>
                        <s:message code="totalesDiezmo.label"/>:<fmt:formatNumber type="currency" currencySymbol="$" value="${totalesDiezmo}" />
                        </span>
                    </td>
                </tr>
            </table>
        </h5>
       
            <h3>
                ${colportor.clave}&nbsp;${colportor.nombreCompleto}
                
            </h3>
            
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
           
            
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th><s:message code="temporadaColportor.ver.label"/></th>
                        <th><s:message code="boletin.label"/></th>
                        <th><s:message code="diezmo"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${concentradoPorTemporadas}" var="clp" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>${clp.temporadaColportor.temporada.nombre}</td>
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
