

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="ordenPago.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="ordenPago" />
        </jsp:include>

        <div id="nueva-ordenPago" class="content scaffold-list" role="main">
             <h1><s:message code="ordenPago.nuevo.label" /></h1>
            <p class="well">
                &nbsp;
            </p>
            <form:form method="post" action="${flowExecutionUrl}">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
                
                <fieldset>
                    <div class="control-group">
                            <div class="required-indicator">RFC:</div>
                            <input type="text" name="rfc" />
                    </div>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="_eventId_rfcCapturado" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="${flowExecutionUrl}&_eventId=endState"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#rfc').focus();
                });
            </script>                    
        </content>
    </body>
</html>
