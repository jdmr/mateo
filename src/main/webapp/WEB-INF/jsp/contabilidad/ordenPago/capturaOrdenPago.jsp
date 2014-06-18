

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
            <form:form commandName="ordenPago" method="post" action="${flowExecutionUrl}">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
                
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="ordenPago.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="128" required="true" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="ordenPago.cheque">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="cheque">
                                <s:message code="cheque.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="cheque" cssClass="span3" />
                            <form:errors path="cheque" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="ordenPago.fechaPago">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaPago">
                                <s:message code="fechaPago.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaPago" maxlength="10" required="true" />
                            <form:errors path="fechaPago" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="_eventId_ordenCapturada" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='_eventId_cancel'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
