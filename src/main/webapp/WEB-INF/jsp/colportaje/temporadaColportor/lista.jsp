

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="temporadaColportor.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="temporadaColportor" />
        </jsp:include>

        <h1><s:message code="temporadaColportor.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/temporadaColportor' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/temporadaColportor/nueva'/>"><i class="icon-user icon-white"></i> <s:message code='temporadaColportor.nueva.label' /></a>
                <input name="filtro" id="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                <button type="submit" class="btn"><s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${temporadaColportores != null}">
                <s:bind path="temporadaColportores.*">
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
            
            <table id="lista" class="table">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="temporada" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="colportor" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="colegio" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="objetivo" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fecha" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="status" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="observaciones" />
                        </jsp:include>                        
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="asociado" />
                        </jsp:include>
                        
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${temporadaColportores}" var="temporadacolportor" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/colportaje/temporadaColportor/ver/${temporadacolportor.id}' />">${temporadacolportor.temporada.nombre}</a></td>
                            <td>${temporadacolportor.colportor.nombreCompleto}</td>
                            <td>${temporadacolportor.colegio.nombre}</td>
                            <td><div align="right"><fmt:formatNumber type="currency" currencySymbol="$" value="${temporadacolportor.objetivo}" /></div></td>
                            <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${temporadacolportor.fecha}" /></td>
                            <td>${temporadacolportor.status}</td>
                            <td>${temporadacolportor.observaciones}</td>
                            <td>${temporadacolportor.asociado.nombreCompleto}</td>
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
            
            $( "#filtro" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/colportor/get_colportor_list',
                select: function(event, ui) {
                        $("input#filtro").val(ui.item.nombre);
                        
                        return false;
                    }
            })
            });
          </script>
        </content>
    </body>
</html>
