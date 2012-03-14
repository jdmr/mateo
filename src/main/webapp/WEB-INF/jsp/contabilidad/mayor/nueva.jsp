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
        <title><s:message code="cuentaMayor.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="mayor" />
        </jsp:include>

        <div id="nueva-mayor" class="content scaffold-list" role="main">
            <h1><s:message code="cuentaMayor.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/mayor'/>"><i class="icon-list icon-white"></i> <s:message code='cuentaMayor.lista.label' /></a>
            </p>
            <form:form commandName="mayor" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="mayor.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.nombreFiscal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreFiscal">
                                <s:message code="nombreFiscal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreFiscal" maxlength="128" required="true" />
                            <form:errors path="nombreFiscal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="128" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.detalle">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="detalle">
                                <s:message code="detalle.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="detalle" cssClass="span3" />
                            <form:errors path="detalle" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.aviso">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="aviso">
                                <s:message code="aviso.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="aviso" cssClass="span3" />
                            <form:errors path="aviso" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.auxiliar">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="auxiliar">
                                <s:message code="auxiliar.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="auxiliar" cssClass="span3" />
                            <form:errors path="auxiliar" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.iva">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="iva">
                                <s:message code="iva.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="iva" cssClass="span3" />
                            <form:errors path="iva" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.porcentajeIva">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="porcentajeIva">
                                <s:message code="porcentajeIva.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="porcentajeIva" maxlength="128" required="true" />
                            <form:errors path="porcentajeIva" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/contabilidad/mayor'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
