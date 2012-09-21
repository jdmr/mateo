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
        <title><s:message code="activo.sube.imagen.label" arguments="${activo.folio}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="nuevo-tipoActivo" class="content scaffold-list" role="main">
            <h1><s:message code="activo.sube.imagen.label" arguments="${activo.folio}" /></h1>
            <hr/>

            <c:url var="subeUrl" value="/activoFijo/activo/subeImagen" />
            <form action="${subeUrl}" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="activoId" value="${activo.id}" />
                <fieldset>
                    <div class="row-fluid">
                        <div class="control-group">
                            <label for="imagen">
                                <s:message code="imagen.label" />
                            </label>
                            <input name="imagen" type="file" />
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
                $('input#imagen').focus();
            });
        </script>                    
    </content>
</body>
</html>
