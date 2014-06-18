<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../../idioma.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeMensualAsociado.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensualAsociado" />
        </jsp:include>

        <h1><s:message code="informeMensualAsociado.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/informes/informeMensualAsociado' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            
            <p class="well">
                <s:message code="mes.label" />
                <select id="mes" name="mes" class="input-large search-query" >
                    <c:forEach items="${meses}" var="mes">
                        <option value="${mes.key}">${mes.value}</option>
                    </c:forEach>
                </select>&nbsp;
                <s:message code="year.label" />
                <input type="text" id="year" name="year" maxlength="10"/>&nbsp;
                <button type="submit" class="btn"><s:message code="buscar.label"/></button>
                <a href="<c:url value='/colportaje/informes/informeMensualDelAsociado/finalizar' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="finalizar.button" /></a>
            </p>
            
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            
            <h3>
                <s:message code="asociado.label" />: <c:out value="${asociadoColportor.nombreCompleto}"/></br>
                <s:message code="mes.label" />: <fmt:formatDate pattern="MMMM/yyyy" timeZone="America/Monterrey" value="${informeMensualAsociadoTotales.fecha}"/>
            </h3>
            
            <table id="totales" class="table">
                <tbody>
                    <tr> 
                        <td>
                            <span class="label label-success">
                                <b><s:message code="hrsTrabajadas.label" /> </b><fmt:formatNumber  maxFractionDigits="0" type="number"  value="${informeMensualAsociadoTotales.hrsTrabajadas}" />
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="literaturaVendida.label" /> </b><fmt:formatNumber type="number" currencySymbol="$" value="${informeMensualAsociadoTotales.literaturaVendida}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="totalPedidos.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${informeMensualAsociadoTotales.totalPedidos}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="totalVentas.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${informeMensualAsociadoTotales.totalVentas}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="literaturaGratis.label" /> </b><fmt:formatNumber type="number" value="${informeMensualAsociadoTotales.literaturaGratis}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="oracionesOfrecidas.label" /></b><fmt:formatNumber type="number" value="${informeMensualAsociadoTotales.oracionesOfrecidas}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="casasVisitadas.label" /></b><fmt:formatNumber type="number" value="${informeMensualAsociadoTotales.casasVisitadas}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="contactosEstudiosBiblicos.label" /></b><fmt:formatNumber type="number" value="${informeMensualAsociadoTotales.contactosEstudiosBiblicos}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="bautizados.label" /></b><fmt:formatNumber type="number" value="${informeMensualAsociadoTotales.bautizados}"/>
                            </span>
                        </td>
                        <td>
                            <span class="label label-success">
                            <b><s:message code="diezmo.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${informeMensualAsociadoTotales.diezmo}"/>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th><s:message code="clave.label" /></th>
                        <th><s:message code="nombre.label" /></th>
                        <th><s:message code="hrsTrabajadas.label" /></th>
                        <th><s:message code="literaturaVendida.label" /></th>
                        <th><s:message code="totalPedidos.label" /></th>
                        <th><s:message code="totalVentas.label" /></th>
                        <th><s:message code="literaturaGratis.label" /></th>
                        <th><s:message code="oracionesOfrecidas.label" /></th>
                        <th><s:message code="casasVisitadas.label" /></th>
                        <th><s:message code="contactosEstudiosBiblicos.label" /></th>
                        <th><s:message code="bautizados.label" /></th>
                        <th><s:message code="diezmo.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${informeMensualAsociado}" var="clp" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>${clp.informeMensual.colportor.clave}</td>
                            <td>${clp.informeMensual.colportor.nombreCompleto}</td>
                            <td><fmt:formatNumber type="number" pattern="###,###" value="${clp.hrsTrabajadas}"/></td>
                            <td>${clp.literaturaVendida}</td>
                            <td><fmt:formatNumber type="decimal" pattern="###,##0.00" value="${clp.totalPedidos}"/></td>
                            <td><fmt:formatNumber type="decimal" pattern="###,##0.00" value="${clp.totalVentas}"/></td>
                            <td>${clp.literaturaGratis}</td>
                            <td>${clp.oracionesOfrecidas}</td>
                            <td>${clp.casasVisitadas}</td>
                            <td>${clp.contactosEstudiosBiblicos}</td>
                            <td>${clp.bautizados}</td>
                            <td><fmt:formatNumber type="decimal" pattern="###,##0.00" value="${clp.diezmo}"/></td>
                            
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
