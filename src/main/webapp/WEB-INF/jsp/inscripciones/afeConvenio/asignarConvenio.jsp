<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="afeConvenio.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="asignarConvenio" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="afeConvenio.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/afeConvenio/asignarConvenio'/>"><i class="icon-list icon-white"></i> <s:message code='afeConvenio.lista.label' /></a>
            </p>
            <form:form commandName="afeConvenio" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="afeConvenio.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="7" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <%-- <s:bind path="afeConvenio.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.diezma">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="diezma">
                                <s:message code="diezma.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="diezma" cssClass="span3" />
                            <form:errors path="diezma" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.numHoras">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="numHoras">
                                <s:message code="numHoras.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="numHoras" maxlength="128" required="true" />
                            <form:errors path="numHoras" cssClass="alert alert-error" />
                        </div>
                    </s:bind> 
                     <s:bind path="afeConvenio.tipoBeca">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoBeca">
                                <s:message code="tiposBecas.label" />
                                <span class="required-indicator">*</span>
                                </label>
                                <form:select id="tipoBeca" path="tipoBeca.id" required="true" cssClass="span3" >
                                    <form:options items= "${tiposBecas}" itemValue="id" itemLabel="descripcion" />
                                </form:select>
                                <form:errors path="tipoBeca" cssClass="alert alert-error" />
                                     
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.importe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="importe">
                                <s:message code="importe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importe" maxlength="128" required="true" />
                            <form:errors path="importe" cssClass="alert alert-error" />
                        </div>
                    </s:bind> --%>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/afeConvenio/asignarConvenio/'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
               
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#descripcion').focus();
            });
        </script>                    
    </content>
</body>
</html>
