<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="clienteColportor.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="clienteColportor" />
        </jsp:include>

        <div id="nuevo-clienteColportor" class="content scaffold-list" role="main">
            <h1><s:message code="clienteColportor.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/clientes'/>"><i class="icon-list icon-white"></i>
                    <s:message code='clienteColportor.lista.label' /></a>
            </p>
            <form:form commandName="clienteColportor" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="clienteColportor.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="120" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.apPaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apPaterno">
                                <s:message code="apPaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apPaterno" maxlength="120" required="true" />
                            <form:errors path="apPaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.apMaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apPaterno">
                                <s:message code="apMaterno.label" />
                            </label>
                            <form:input path="apMaterno" maxlength="120"  />
                            <form:errors path="apMaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.telefonoCasa">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="telefonoCasa">
                                <s:message code="telefonoCasa.label" />
                            </label>
                            <form:input path="telefonoCasa" maxlength="10"  />
                            <form:errors path="telefonoCasa" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.telefonoTrabajo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="telefonoTrabajo">
                                <s:message code="telefonoTrabajo.label" />
                            </label>
                            <form:input path="telefonoTrabajo" maxlength="10"  />
                            <form:errors path="telefonoTrabajo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.telefonoCelular">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="telefonoCelular">
                                <s:message code="telefonoCelular.label" />
                            </label>
                            <form:input path="telefonoCelular" maxlength="10"  />
                            <form:errors path="telefonoCelular" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.email">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="email">
                                <s:message code="email.label" />
                            </label>
                            <form:input path="email" maxlength="200"  />
                            <form:errors path="email" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.direccion1">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="direccion1">
                                <s:message code="direccion1.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="direccion1" maxlength="150" required="true" />
                            <form:errors path="direccion1" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.direccion2">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="direccion2">
                                <s:message code="direccion2.label" />
                            </label>
                            <form:input path="direccion2" maxlength="100"  />
                            <form:errors path="direccion2" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.colonia">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="colonia">
                                <s:message code="colonia.label" />
                            </label>
                            <form:input path="colonia" maxlength="100"  />
                            <form:errors path="colonia" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="clienteColportor.municipio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="municipio">
                                <s:message code="municipio.label" />
                            </label>
                            <form:input path="municipio" maxlength="100"  />
                            <form:errors path="municipio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportaje/ventas/clientes'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    </body>
</html>
