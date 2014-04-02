<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeMensual.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensual" />
        </jsp:include>

        <h1><s:message code="informeMensual.lista.label" /></h1>
        <hr/>
        
        <h4>
            <c:out value="${colportor.clave}"/>
            <c:out value="${colportor.nombreCompleto}"/>
        </h4>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/informeMensual' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <c:if test="${colportor != null}">
                <a class="btn btn-primary" href="<s:url value='/colportaje/informeMensual/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='informeMensual.nuevo.label' /></a>
                </c:if>
                <input id="clave" name="clave" class="input-medium search-query" value="${colportor.clave}">
                <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${informesMensuales != null}">
                <s:bind path="informesMensuales.*">
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
                            <jsp:param name="columna" value="fecha" />
                        </jsp:include>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${informesMensuales}" var="informeMensual" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/colportaje/informeMensual/ver/${informeMensual.id}' />">
                                    <fmt:formatDate pattern="MMMM, yyyy" value="${informeMensual.fecha}" /></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
        </form>        
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
            
            <script>
            $(function() {
            
            $( "#clave" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/colportor/get_colportor_list',
                select: function(event, ui) {
                        $("input#clave").val(ui.item.nombre);
                        
                        return false;
                    }
            })
            });
          </script>
        </content>
    </body>
</html>
