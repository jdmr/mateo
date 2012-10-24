<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="puesto.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="puesto" />
        </jsp:include>

        <div id="nuevo-puesto" class="content scaffold-list" role="main">
            <h1><s:message code="puesto.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/puestos'/>"><i class="icon-list icon-white"></i> <s:message code='puesto.lista.label' /></a>
            </p>
            <form:form commandName="puesto" action="crea" method="post">
                <form:hidden path="id" />
                  <form:hidden path="status" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="puesto.descripcion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="descripcion">
                                        <s:message code="puesto.descripcion.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="descripcion" maxlength="150" required="true" cssClass="span4" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="puesto.categoria">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="categoria">
                                        <s:message code="puesto.categoria.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="categoria" maxlength="5" required="true" cssClass="span4" min="3" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="puesto.seccion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="seccion">
                                        <s:message code="puesto.seccion.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="seccion" maxlength="5" required="true" cssClass="span4" min="3" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="puesto.minimo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="minimo">
                                        <s:message code="puesto.minimo.label" />
                                    </label>
                                    <form:input path="minimo" maxlength="5" cssClass="span4" min="2"/>
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="puesto.maximo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="maximo">
                                        <s:message code="puesto.maximo.label" />
                                    </label>
                                    <form:input path="maximo" maxlength="5" cssClass="span4" min="2" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="puesto.rangoAcademico">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="rangoAcademico">
                                        <s:message code="puesto.rangoAcademico.label" />
                                    </label>
                                    <form:input path="rangoAcademico" maxlength="5" cssClass="span4" min="2" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/puestos'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
