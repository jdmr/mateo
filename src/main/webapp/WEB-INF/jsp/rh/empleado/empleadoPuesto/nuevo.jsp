<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleadoPuesto.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleadoPuesto" />
        </jsp:include>

        <div id="nuevo-perded" class="content scaffold-list" role="main">
            <h1><s:message code="empleadoPuesto.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleado/empleadoPuesto/'/>"><i class="icon-list icon-white"></i> <s:message code='empleadoPuesto.lista.label' /></a>
            </p>
            <form:form commandName="empleadoPuestoForm" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <fieldset>
                    <s:bind path="empleadoPuestoForm.puesto">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="puesto">
                                <s:message code="empleadoPuesto.puesto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select id="puesto" path="puesto.id" items="${puestoList}" itemLabel="descripcion" itemValue="id" />
                            <form:errors path="puesto" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleadoPuestoForm.turno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="turno">
                                <s:message code="empleadoPuesto.turno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="turno" maxlength="100" required="true" cssClass="span4" />
                        </div>
                    </s:bind>



                    <div class="control-group">
                        <label for='username'><s:message code="centroCosto.label" /></label>
                        <input type='text'  name="CCId" id="CCId" />
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/empleadoPuesto'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
