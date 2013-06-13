<%-- 
    Document   : asigaRoles
    Created on : 14-may-2013, 9:08:23
    Author     : semdariobarbaamaya
--%>

%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s"    uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="inicializa.title" /></title>
    </head>
    <body>
        <h2><s:message code="inicializa.title" /></h2>
        <form action="asignaRoles" method="post">
            <fieldset>
                <div class="control-group">
                    <label for='username'><s:message code="login.username" /></label>
                    <input id="username" name="username" type="text" value='<c:out value="${usertmp.username}"/>'/>
                </div>
                    
                    
                    <c:if test="${usertmp.id != null}">
                        
                        <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span1"><s:message code="nombre.label" /></div>
                        <div class="span11">${usertmp.nombre}</div>
                        </div>
                        
                        <input id="username" name="roles" type="text" value='<c:out value="${roles}"/>'/>
                        
                        <br><c:out value="${rolesTodos}"/>

                    </c:if>
                    
                    
                    
                <div class="control-group">
                    <br><input type="submit" value="<s:message code='inicializa.title' />" class="btn btn-large btn-primary" style="width:170px;"/>
                </div>
            </fieldset>
        </form>
    </body>
</html>
<content>
    <script>
        $(document).ready(function() {
            $('input#username').focus();
        });
    </script>                    
</content>