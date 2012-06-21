<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="inicio.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>
        <h1><s:message code="inicio.label" /></h1>
        <c:if test="${not empty message}">
            <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <div class="alert alert-block alert-info fade in" role="status">
            <a class="close" data-dismiss="alert">×</a>
            <h1 class="alert-heading">Novedades</h1>
            <p>Vamos a estar usando este lugar para ir informándoles acerca de las novedades y arreglos a problemas (bugs) del sistema:</p>
            <h2 class="alert-heading">Inventario</h2>
            <ul>
                <li>
                    En la lista de salidas y entradas sólo se muestran las del mes en curso, si desean ver más, opriman la flecha que se encuentra
                    a lado del botón Buscar y encontrarán la opción "Buscar por Fecha". Pueden meter sólo la fecha inicial y va a buscar hasta la
                    fecha actual. Si ponen las dos fechas va a buscar sólo las que se encuentren en ese rango.
                </li>
                <li>
                    Hemos cambiado de lugar la búsqueda por proveedor, ya no va a funcionar si lo ponen en la cajita de búsqueda, necesitan oprimir
                    también la flecha que se encuentra a lado del botón Buscar y van a encontrar la opción "Buscar por Proveedor".
                </li>
                <li>
                    Se ha agregado la opción de inhabilitar los productos que no se usen más (no podemos borrarlos porque existe un historial en el
                    que se les hace referencia). Simplemente editen el producto y al final hay una opción que dice "Inactivo", le dan clic en la
                    caja que está a un lado y le dan clic al botón actualizar para deshabilitarlo, y no va a aparecer en las listas, ni en los
                    productos a los que se les puede dar entrada o salida. Si desean habilitarlo nuevamente, encontrarán una opción en la lista
                    de productos a lado del botón "Buscar por Fecha" que dice "¿Solo inactivos?", le dan clic en la cajita en el lado izquierdo
                    de este mensaje y luego al botón "Buscar" y les mostrará todos los productos que tengan inactivos, si son muchos puede usar
                    la caja de búsqueda para filtrarlos y encontrarlo más fácil. Una vez encontrado, lo edita y hace el mismo procedimiento para 
                    inhabilitarlo, pero esta vez para habilitarlo nuevamente, y aparecerá nuevamente en las listas y demás lugares.
                </li>
                <li>
                    Estamos trabajando para darles la posibilidad de imprimir la lista de productos por fecha (funcionalidad con la que ya
                    contaban en la versión anterior). En cuanto la tengamos, les avisaremos por este medio.
                </li>
            </ul>
            <h3 class="alert-heading">Gracias.</h3>
        </div>
    </body>
</html>
