
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:message code="temporadaColportor.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="temporadaColportor" />
        </jsp:include>

        <div id="edita-temporadaColportor" class="content scaffold-list" role="main">
            <h1><s:message code="temporadaColportor.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='temporadaColportor.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="../actualiza" />
            <form:form commandName="temporadaColportor" method="post" action="${actualizaUrl}">
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
                <form:hidden path="fecha" />

                <fieldset>
                    <s:bind path="temporadaColportor.temporada">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="temporada">
                                <s:message code="temporada.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="temporadaId" path="temporada.id" items="${temporadas}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="temporada" cssClass="alert alert-error" />
                        </div>
                    </s:bind>                    
                    <s:bind path="temporadaColportor.colportor">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="colportor">
                                <s:message code="colportor.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="colportorId" path="colportor.id" items="${colportores}" itemLabel="calle" itemValue="id" />
                                <form:errors path="colportor" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                   <s:bind path="temporadaColportor.colegio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="colegio">
                                <s:message code="colegio.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="colegioId" path="colegio.id" items="${colegios}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="colegio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.objetivo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="objetivo">
                                <s:message code="objetivo.label" />
                                <span class="required-indicator">*</span>
                                <form:input path="objetivo" maxlength="50" required="true"  />
                                <form:errors path="objetivo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                                <form:radiobutton path="status"  value="A" cssClass="span3" id="status"/><s:message code="activo.label"/>
                                <form:radiobutton path="status"  value="I"  cssClass="span3" id="status"/><s:message code="inactivo.label"/>
                                <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="temporadaColportor.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <form:textarea path="observaciones" maxlength="4000" required="true"  />
                                <form:errors path="observaciones" cssClass="alert alert-error" type="texttarea"/>
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="actualiza" value="<s:message code='actualizar.button' />" class="btn btn-large btn-primary" />
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