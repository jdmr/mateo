<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="detalle.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="detalle" />
        </jsp:include>

        <h1><s:message code="detalle.lista.label" /></h1>
        <hr/>

        <c:if test="${not empty message}">
            <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <c:if test="${detalles != null}">
            <s:bind path="detalles.*">
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
                        <jsp:param name="columna" value="folio" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="ccp" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="proveedor" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="rfc" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="iva" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="subtotal" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="total" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="status" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="fechaFactura" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="informeEmpleado" />
                    </jsp:include>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${detalles}" var="detalle" varStatus="status">
                    <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                        <td>${detalle.folioFactura}</td>                            
                        <td>${detalle.ccp}</td>
                        <td>${detalle.nombreProveedor}</td>
                        <td>${detalle.RFCProveedor}</td>
                        <td>${detalle.IVA}</td>
                        <td>${detalle.subtotal}</td>
                        <td>${detalle.total}</td>
                        <td>${detalle.status}</td>
                        <td>${detalle.fechaFactura}</td>
                        <td>${detalle.informeEmpleado.id}</td>
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
