<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<th>
    <a href="javascript:ordena('${param.columna}');">
        <s:message code="${param.columna}.label" />
        <c:choose>
            <c:when test="${param.order == param.columna && param.sort == 'asc'}">
                <i class="icon-chevron-up"></i>
            </c:when>
            <c:when test="${param.order == param.columna && param.sort == 'desc'}">
                <i class="icon-chevron-down"></i>
            </c:when>
        </c:choose>
    </a>
</th>
