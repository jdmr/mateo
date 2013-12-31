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
            <jsp:param name="menu" value="concentradoGral" />
        </jsp:include>

        <h1><s:message code="concentradoPorTemporadas.label" /></h1>
        <hr/>
        
        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/reportes/concentradoGeneralPorTemporadas' />">
        <p class="well">
                <s:message code="colportor.clave.label"/><input id="clave" name="clave" class="input-medium search-query" value="${colportor.clave}">
                <button id="btnSmt" type="submit" class="btn"><s:message code="buscar.label" /></button>
        </p>
        </form>
            
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
                            <td>
                                <c:url var="linky" value="/colportaje/documento/lista">
                                    <c:param name="clave" value="${clp.colportor.clave}"/>
                                    <c:param name="temporadaId" value="${clp.temporadaColportor.temporada.id}"/>
                                </c:url>
                                <a alt="Documentos del Colportor" target="_blank"href='<c:out value="${linky}"/>'>${clp.temporadaColportor.temporada.nombre}</a>
                            </td>
                            <td><fmt:formatNumber type="currency" currencySymbol="$" value="${clp.acumuladoBoletin}" /></td>
                            <td><fmt:formatNumber type="currency" currencySymbol="$" value="${clp.acumuladoDiezmo}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
            <script>
            $(function() {
            
            $( "#clave" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/colportor/get_colportor_list',
                select: function(event, ui) {
                        $("input#clave").val(ui.item.nombre);
                        $("button#btnSmt").focus();
                        return false;
                    }
            })
            });
          </script>
        </content>
    </body>
</html>
