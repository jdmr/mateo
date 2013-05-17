

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="ordenPago.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="ordenPago" />
        </jsp:include>

        <div id="edita-ordenPago" class="content scaffold-list" role="main">
            <h1><s:message code="ordenPago.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/ordenPago'/>"><i class="icon-list icon-white"></i> <s:message code='ordenPago.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/contabilidad/ordenPago/actualiza" />
            <form:form commandName="ordenPago" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <form:hidden path="id" />
                <form:hidden path="version" />

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
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/contabilidad/ordenPago/ver/${ordenPago.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#nombre').focus();
                });
            </script>                    
        </content>
    </body>
</html>
