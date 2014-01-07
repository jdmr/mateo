<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="jefe.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="jefe" />
        </jsp:include>

        <div id="nuevo-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="jefe.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/jefe'/>"><i class="icon-list icon-white"></i> <s:message code='jefe.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/jefe/graba" />
            <form:form commandName="jefe" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="jefe.centroCosto">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="centroCosto">
                                <s:message code="centroCosto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input id="nombreCentroCosto" path="centroCosto.nombre" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="centroCosto" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="jefe.jefe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="jefe">
                                <s:message code="jefe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input id="nombreJefe" path="jefe.nombre" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="jefe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="jefe.subjefe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="subjefe">
                                <s:message code="subjefe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input id="nombreJefe" path="subjefe.nombre" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="subjefe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>


                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/jefe/ver/${diaFeriado.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#centroCosto').focus();

            });
        </script>                    
    </content>
</body>
</html>
