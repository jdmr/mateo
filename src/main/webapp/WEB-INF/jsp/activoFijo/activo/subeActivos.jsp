<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.sube.activos.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="nuevo-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.sube.activos.label" /></h1>
            <hr/>

            <c:url var="subeUrl" value="/activoFijo/activo/subeActivos" />
            <form action="${subeUrl}" method="POST" enctype="multipart/form-data">
                <fieldset>
                    <div class="row-fluid">
                        <div class="control-group">
                            <label for="archivo">
                                <s:message code="activo.excel.label" />
                            </label>
                            <input name="archivo" type="file" />
                        </div>
                    </div>
                            
                    <div class="row-fluid">
                        <div class="control-group">
                            <label for="codigo">
                                <s:message code="codigo.label" />
                            </label>
                            <input id="codigo" name="codigo" type="number" min="0" step="1" value="0" />
                        </div>
                    </div>
                            
                    <div class="row-fluid">
                        <div class="control-group">
                            <button type="submit" class="btn btn-primary btn-large"><i class="icon-upload icon-white"></i> <s:message code="sube.button" /></button>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#archivo').focus();
            });
        </script>                    
    </content>
</body>
</html>
