<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <li<c:if test="${param.menu eq 'clienteColportor'}"> class="active"</c:if>><a href="<c:url value='/colportaje/ventas/clientes' />"><s:message code="clienteColportor.label" /></a></li>
    </ul>
</nav>
    
