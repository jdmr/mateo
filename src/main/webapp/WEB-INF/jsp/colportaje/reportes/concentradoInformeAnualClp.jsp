<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeAnual.label" /> </title>
    </head>
    <body>
        <jsp:include page="./menu.jsp" >
            <jsp:param name="menu" value="concentradoInformesAnuales" />
        </jsp:include>

        <h1><s:message code="informeAnual.label" /></h1>
        <hr/>
        
        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/reportes/concentradoInformesAnuales' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="observaciones" id="observaciones" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <sec:authorize access="hasRole('ROLE_ASOC')">
                    <s:message code="colportor.label" />
                    <input id="clave" name="clave" class="input-medium search-query" value="${colportor.clave}">
                    <button type="submit" class="btn"><s:message code="buscar.label" /></button>
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_CLP')">
                    <h4>
                    ${colportor.clave} ${colportor.nombreCompleto}   
                    </h4>
                </sec:authorize>
            </p> 
        </form>
        <table id="totales" class="table">
            <tbody>
                <tr> 
                    <td>
                        <span class="label label-success">
                        <b><s:message code="hrsTrabajadas.label" /> </b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.hrsTrabajadas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="literaturaVendida.label" /> </b><fmt:formatNumber type="number" currencySymbol="$" value="${concentradoInformeAnualClpTotales.literaturaVendida}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="totalPedidos.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${concentradoInformeAnualClpTotales.totalPedidos}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="totalVentas.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${concentradoInformeAnualClpTotales.totalVentas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="diezmo.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${concentradoInformeAnualClpTotales.diezmo}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="literaturaGratis.label" /> </b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.literaturaGratis}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="oracionesOfrecidas.label" /></b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.oracionesOfrecidas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="casasVisitadas.label" /></b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.casasVisitadas}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="contactosEstudiosBiblicos.label" /></b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.contactosEstudiosBiblicos}"/>
                        </span>
                    </td>
                    <td>
                        <span class="label label-success">
                        <b><s:message code="bautizados.label" /></b><fmt:formatNumber type="number" value="${concentradoInformeAnualClpTotales.bautizados}"/>
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
                    <c:forEach items="${concentradoInformeAnualClp}" var="detalle" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>
                                ${detalle.mesInforme}
                            </td>
                            
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                <fmt:formatNumber type="number" pattern="###,###" value="${detalle.hrsTrabajadas}"/>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.literaturaVendida}</td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.totalPedidos}"/>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.totalVentas}"/>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.literaturaGratis}
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.oracionesOfrecidas}
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.casasVisitadas}
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.contactosEstudiosBiblicos}
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                ${detalle.bautizados}
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    </b>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
                                    <b>
                                </c:if>
                                <fmt:formatNumber type="decimal" pattern="###,##0.00" value="${detalle.diezmo}"/>
                                <c:if test="${detalle.informeMensual.status eq '@'}">
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
            <script>
            $(function() {
            
            $( "#clave" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/colportor/get_colportor_list',
                select: function(event, ui) {
                        $("input#clave").val(ui.item.nombre);
                        $("select#year").focus();
                        return false;
                    }
            })
            });
          </script>
        </content>
    </body>
</html>
