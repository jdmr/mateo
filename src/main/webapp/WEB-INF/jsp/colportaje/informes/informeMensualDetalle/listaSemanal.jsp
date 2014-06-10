<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../../idioma.jsp"%>
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
            
        <table id="totales" class="table">
            <tbody>
                <tr> 
                    <td>
                        <span class="label label-success">
                        <b><s:message code="hrsTrabajadas.label" /> </b><fmt:formatNumber type="number" value="${totales.hrsTrabajadas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="literaturaVendida.label" /> </b><fmt:formatNumber type="number" currencySymbol="$" value="${totales.literaturaVendida}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="totalPedidos.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${totales.totalPedidos}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="totalVentas.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${totales.totalVentas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="diezmo.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${totales.diezmo}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="literaturaGratis.label" /> </b><fmt:formatNumber type="number" value="${totales.literaturaGratis}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="oracionesOfrecidas.label" /></b><fmt:formatNumber type="number" value="${totales.oracionesOfrecidas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="casasVisitadas.label" /></b><fmt:formatNumber type="number" value="${totales.casasVisitadas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="contactosEstudiosBiblicos.label" /></b><fmt:formatNumber type="number" value="${totales.contactosEstudiosBiblicos}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="bautizados.label" /></b><fmt:formatNumber type="number" value="${totales.bautizados}"/>
                        </span>
                    </td>
                </tr>
            </tbody>
        </table>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/informes/informeMensualDetalle' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            
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
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="diezmo" />
                        </jsp:include>
                    </tr>
                </thead>
                <tbody>
                    ${informeMensualDetalle}
                    <c:forEach items="${informeMensualDetalles}" var="detalle" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>
                                <%/*
                                <c:if test="${detalle.informeMensual.status ne '@'}">
                                    <c:if test="${empty detalle.id}">
                                        <a href="<c:url value='/colportaje/informes/informeMensualDetalle/nuevoReg/${detalle.fecha}' />">
                                    </c:if>
                                    <c:if test="${not empty detalle.id}">
                                        <a href="<c:url value='/colportaje/informes/informeMensualDetalle/ver/${detalle.id}' />">
                                    </c:if>
                                        <fmt:formatDate pattern="EEEE dd/MMM" value="${detalle.fecha}" />
                                    </a>
                                </c:if>                                
                                */%>
                            </td>
                            
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    <fmt:formatNumber type="number" pattern="###,###" value="${detalle.hrsTrabajadas}"/>
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.literaturaVendida}</td>
                                    </b>
                                </c:if>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.totalPedidos}"/>
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.totalVentas}"/>
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.literaturaGratis}
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.oracionesOfrecidas}
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.casasVisitadas}
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.contactosEstudiosBiblicos}
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    ${detalle.bautizados}
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                    <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.diezmo}"/>
                                    </b>
                                </c:if>
                            </td>
                            
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
