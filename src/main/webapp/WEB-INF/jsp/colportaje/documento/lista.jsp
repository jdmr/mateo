
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
    <head>
         
         <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">

         
            
        <title><s:message code="documento.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="documento" />
        </jsp:include>     
            
        <h1><s:message code="documento.lista.label" /></h1>        

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/colportaje/documento' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="observaciones" id="observaciones" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />

            <p class="well">
                <c:if test="${temporadaColportor != null}">
                <a class="btn btn-primary" href="<s:url value='/colportaje/documento/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='documento.nuevo.label' /></a>
                </c:if>
                <input id="clave" name="clave" class="input-medium search-query" value="${colportor.clave}">
                
                <label for="temporada">
                <s:message code="temporada.label" />
                <select id="temporadaId" name="temporadaId" id="temporadaId" class="input-large search-query" value="${temporadaColportor.id}">
                    <c:forEach items="${temporadas}" var="temporada">
                        <c:if test="${temporada.nombre eq temporadaColportor.temporada.nombre}">
                            <option value="${temporada.id}" selected="true">${temporada.nombre}</option>
                        </c:if>
                        <c:if test="${not (temporada.nombre eq temporadaColportor.temporada.nombre)}">
                            <option value="${temporada.id}">${temporada.nombre}</option>
                        </c:if>
                        
                    </c:forEach>
                </select>
                <button type="submit" class="btn"><s:message code="buscar.label" /></button>
            </p>   
            
            <h3>
                ${temporadaColportor.colportor.clave}&nbsp;${temporadaColportor.colportor.nombreCompleto}
                -
                ${temporadaColportor.temporada.nombre}
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <c:if test="${not (temporadaColportor eq null)}">
                    <a class="btn btn-primary" target="_blank" href="<s:url value='/colportaje/reportes/concentradoPorTemporadas?colportorId=${temporadaColportor.colportor.id}'/>"><i class="icon-user icon-white"></i> <s:message code='concentradoPorTemporadas.label' /></a>
                </c:if>
            </h3>
            
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
                        <td><b><s:message code="compras.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${Total_Boletin}"/></td>
                        <td><b><s:message code="objetivo.label" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${temporadaColportor.objetivo}"/></td>
                        <td><b><s:message code="pctAlcanzado.label" /> </b><fmt:formatNumber type="percent" currencySymbol="%" value="${Alcanzado*10}"/></td>
                        <td><b><s:message code="diezmo" /> </b><fmt:formatNumber type="currency" currencySymbol="$" value="${Total_Diezmos}"/></td>
                        <td><b><s:message code="fidelidad" /> </b><fmt:formatNumber type="percent" currencySymbol="%" value="${Fidelidad*10}"/></td>
                        <td><b><s:message code="depositos" /></b><fmt:formatNumber type="currency" currencySymbol="$" value="${Total_Depositos}"/></td>
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
                            <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${documento.fecha}" /></td>
                            <td>${documento.folio}</td>
                            <td><div align="right"><fmt:formatNumber type="currency" currencySymbol="$" value="${documento.importe}" /></div></td>
                            <td>${documento.observaciones}</td>
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
                        $("#temporadaId").html('');
                        $.getJSON("${pageContext. request. contextPath}/colportaje/temporadaColportor/get_temporada_clp_list?clave="+ui.item.nombre)
                        .done(function (result) {
                            $.each(result, function(idx, item){
                                $("#temporadaId").append($("<option value=\""+item.id+"\">"+item.value+"</option>"));
                            });
                        });
                        $("select#temporadaId").focus();
                        return false;
                    }
            })
            });
          </script>
    </content>
</body>
</html>
