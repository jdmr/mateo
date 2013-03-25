<%-- 
    Document   : nuevo
    Created on : Jan 27, 2012, 10:37:52 AM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="ejercicio.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="ejercicio" />
        </jsp:include>

        <div id="nueva-ejercicio" class="content scaffold-list" role="main">
            <h1><s:message code="ejercicio.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/ejercicio'/>"><i class="icon-list icon-white"></i> <s:message code='ejercicio.lista.label' /></a>
            </p>
            <form:form commandName="ejercicio" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="ejercicio.id.idEjercicio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="idEjercicio">
                                <s:message code="id.idEjercicio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input id="idEjercicio" path="id.idEjercicio" maxlength="128" required="true" />
                            <form:errors path="id.idEjercicio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="ejercicio.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/contabilidad/ejercicio'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#idEjercicio').focus();
                });
            </script>                    
        </content>
    </body>
</html>
