<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<div class="row-fluid">
    <div class="span8">
        <div class="pagination">
            <ul>
                <li class="disabled"><a href="#"><s:message code="mensaje.paginacion" arguments="${paginacion}" /></a></li>
                <c:forEach items="${paginas}" var="paginaId">
                    <li <c:if test="${pagina == paginaId}" >class="active"</c:if>>
                        <a href="javascript:buscaPagina(${paginaId});" >${paginaId}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="span4">
        <div class="btn-group pull-right" style="margin-top: 22px;margin-left: 10px;">
            <button id="enviaCorreoBtn" class="btn" data-loading-text="<s:message code='enviando.label'/>" onclick="javascript:enviaCorreo('XLS');" ><i class="icon-envelope"></i> <s:message code="envia.correo.label" /></button>
            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><a href="javascript:enviaCorreo('PDF');"><img src="<c:url value='/images/pdf.gif' />" /></a></li>
                <li><a href="javascript:enviaCorreo('CSV');"><img src="<c:url value='/images/csv.gif' />" /></a></li>
                <li><a href="javascript:enviaCorreo('XLS');"><img src="<c:url value='/images/xls.gif' />" /></a></li>
            </ul>
        </div>
        <p class="pull-right" style="margin-top: 20px;">
            <a href="javascript:imprime('PDF');"><img src="<c:url value='/images/pdf.gif' />" /></a>
            <a href="javascript:imprime('CSV');"><img src="<c:url value='/images/csv.gif' />" /></a>
            <a href="javascript:imprime('XLS');"><img src="<c:url value='/images/xls.gif' />" /></a>
        </p>
    </div>
</div>
