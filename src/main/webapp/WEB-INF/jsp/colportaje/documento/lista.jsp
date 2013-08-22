<%-- 
    Document   : lista
    Created on : 4/04/2012, 09:49:49 AM
    Author     : wilbert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="documento.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="documento" />
        </jsp:include>

        <h1><s:message code="documento.lista.label" /></h1>
        <h3>  ${temporadaColportor.colportor.clave}&nbsp;${temporadaColportor.colportor.nombre}&nbsp;${temporadaColportor.colportor.apPaterno}&nbsp;${temporadaColportor.colportor.apMaterno}  </h3>
        <hr/>


        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/documento' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="observaciones" id="observaciones" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />


            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/documento/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='documento.nuevo.label' /></a>
                <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">
                <button type="submit" class="btn"><s:message code="buscar.label" /></button>

            </p>
            
            <sec:authorize access="hasRole('ROLE_ASOC')">
                <p>
                    <s:message code="buscarColportor.label" />
                    <input name="clave" type="text" class="input-medium search-query" value="${clave}">
                    <button type="submit" class="btn"><s:message code="buscar.label" /></button>  
                </p>

            </sec:authorize>
            <fieldset>
                <div class="control-group">
                    <label for="temporada">
                        <s:message code="temporada.label" />
                        <span class="required-indicator">*</span>
                        <select id="temporadaId" name="temporadaId">
                            <c:forEach items="${temporadas}" var="temporada">
                                <option value="${temporada.id}">${temporada.nombre}</option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="btn"><s:message code="buscar.label" /></button>    
                </div>
            </fieldset>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${documento != null}">
                <s:bind path="documento.*">
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

            <table id="totales" class="table">
                <tbody>
                    <tr> 
                        <td><b>compras.label </b>${Total_Boletin}</td>
                        <td><b>objetivo.label </b>${temporadaColportor.objetivo}</td>
                        <td><b>pctAlcanzado </b>${Alcanzado} %</td>
                        <td><b>diezmo.label </b>${Total_Diezmos}</td>
                        <td><b>fidelidad.label </b>${Fidelidad} %</td>
                        <td><b>depositos.label $</b> ${Total_Depositos}</td>
                    </tr>
                </tbody>
            </table>
            <table id="lista" class="table">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="tipoDeDocumento" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fecha" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="importe" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="observaciones" />
                        </jsp:include>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${documentos}" var="documento" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/colportaje/documento/ver/${documento.id}' />">${documento.tipoDeDocumento}</a></td>
                            <td>${documento.fecha}</td>
                            <td>${documento.folio}</td>
                            <td>${documento.importe}</td>
                            <td>${documento.observaciones}</td>
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
