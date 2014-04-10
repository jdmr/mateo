<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeMensualDetalle.lista.label" /> de <fmt:formatDate pattern="MMMM/yyyy" value="${informeMensual.fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensualDetalle" />
        </jsp:include>

        <h1><s:message code="informeMensualDetalle.lista.label" /> de <fmt:formatDate pattern="MMMM/yyyy" value="${informeMensual.fecha}" /></h1>
        <hr/>
        
        <h4>
            <c:out value="${colportor.clave}"/>
            <c:out value="${colportor.nombreCompleto}"/>
            
        </h4>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/informeMensualDetalle' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/informeMensualDetalle/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='informeMensualDetalle.nuevo.label' /></a>
                <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${informeMensualDetalle != null}">
                <s:bind path="informeMensualDetalle.*">
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
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="hrsTrabajadas" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="literaturaVendida" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="totalPedidos" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="totalVentas" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="diezmo" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="literaturaGratis" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="oracionesOfrecidas" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="casasVisitadas" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="contactosEstudiosBiblicos" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="bautizados" />
                        </jsp:include>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${informeMensualDetalles}" var="detalle" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>
                                <c:if test="${detalle.informeMensual.status ne '@'}">
                                <a href="<c:url value='/colportaje/informeMensualDetalle/ver/${detalle.id}' />">
                                    <fmt:formatDate pattern="EEEE dd/MMM" value="${detalle.fecha}" />
                                </a>
                                </c:if>
                            </td>
                            <td>${detalle.hrsTrabajadas}</td>
                            <td>${detalle.literaturaVendida}</td>
                            <td>${detalle.totalPedidos}</td>
                            <td>${detalle.totalVentas}</td>
                            <td>${detalle.diezmo}</td>
                            <td>${detalle.literaturaGratis}</td>
                            <td>${detalle.oracionesOfrecidas}</td>
                            <td>${detalle.casasVisitadas}</td>
                            <td>${detalle.contactosEstudiosBiblicos}</td>
                            <td>${detalle.bautizados}</td>
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
