<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeProveedorDetalle.contrarecibo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedorDetalle" />
        </jsp:include>

        <h1><s:message code="informeProveedorDetalle.contrarecibo.label" /></h1>
        <hr/>

        <c:if test="${not empty message}">
            <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <c:if test="${informesProveedorDetalle != null}">
            <s:bind path="informesProveedorDetalle.*">
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


        <div class="row-fluid" style="padding-bottom: 10px;">
            <div class="span1"><s:message code="proveedor.label" /></div>
            <div class="span11">${proveedorFacturas.nombre}</div>
        </div>
        <div class="row-fluid" style="padding-bottom: 10px;">
            <div class="span1"><s:message code="rfc.label" /></div>
            <div class="span11">${proveedorFacturas.rfc}</div>
        </div>

        <table id="lista" class="table table-striped table-hover">
            <thead>
                <tr>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="folio" />
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
                        <jsp:param name="columna" value="dctoProntoPago" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="status" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="fechaFactura" />
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                        <jsp:param name="columna" value="informeProveedor" />
                    </jsp:include>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${informesProveedorDetalle}" var="informeProveedorDetalle" varStatus="status">
                    <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                        <td>${informeProveedorDetalle.folioFactura}</td>                            
                        <td>${informeProveedorDetalle.nombreProveedor}</td>
                        <td>${informeProveedorDetalle.RFCProveedor}</td>
                        <td>${informeProveedorDetalle.IVA}</td>
                        <td>${informeProveedorDetalle.subtotal}</td>
                        <td>${informeProveedorDetalle.total}</td>
                        <td>${informeProveedorDetalle.dctoProntoPago}</td>
                        <td>${informeProveedorDetalle.status}</td>
                        <td>${informeProveedorDetalle.fechaFactura}</td>
                        <td>${informeProveedorDetalle.informeProveedor.id}</td>
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
