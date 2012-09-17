<%-- 
    Document   : sube
    Created on : Jul 9, 2012, 2:56:57 PM
    Author     : J. David Mendoza <jdmendoza@um.edu.mx>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.depreciar.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <div id="nuevo-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.depreciar.label" /></h1>
            <hr/>

            <c:url var="depreciarUrl" value="/activoFijo/activo/depreciar" />
            <form action="${depreciarUrl}" method="POST" >
                <fieldset>
                    <div class="row-fluid">
                        <div class="control-group">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                            </label>
                            <input name="fecha" id="fecha" value="" />
                        </div>
                    </div>

                    <%--
                    <div class="row-fluid">
                        <div class="control-group">
                            <label for="meses">
                                <s:message code="meses.label" />
                            </label>
                            <input name="meses" id="meses" value="12" type="number" min="0" />
                        </div>
                    </div>
                    --%>

                    <div class="row-fluid">
                        <div class="control-group">
                            <button type="submit" class="btn btn-warning btn-large"><i class="icon-time icon-white"></i> <s:message code="depreciar.button" /></button>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option","firstDay",0);
                $("input#fecha").focus();
            });
        </script>                    
    </content>
</body>
</html>
