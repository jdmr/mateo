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
        <title><s:message code="empresa.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empresa" />
        </jsp:include>

        <div id="nueva-empresa" class="content scaffold-list" role="main">
            <h1><s:message code="empresa.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/empresa'/>"><i class="icon-list icon-white"></i> <s:message code='empresa.lista.label' /></a>
            </p>
            <form:form commandName="empresa" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="empresa.codigo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="codigo">
                                <s:message code="codigo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="codigo" maxlength="128" required="true" />
                            <form:errors path="codigo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empresa.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empresa.nombreCompleto">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreCompleto">
                                <s:message code="nombreCompleto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreCompleto" maxlength="128" required="true" />
                            <form:errors path="nombreCompleto" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empresa.rfc">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="rfc">
                                <s:message code="rfc.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="rfc" maxlength="13" required="true" />
                            <form:errors path="rfc" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empresa.centroCosto">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="cuentaNombre">
                                <s:message code="centroCosto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <input type="hidden" name="cuentaId" id="cuentaId" value="${empresa.centroCosto.id.idCosto}" />
                            <input type="text" name="cuentaNombre" id="cuentaNombre" value="${empresa.centroCosto.nombreCompleto}" required="true" class="span6" />
                            <form:errors path="centroCosto" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/admin/empresa'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#cuentaNombre').autocomplete({
                        source: "<c:url value='/admin/empresa/centrosDeCosto' />",
                        select: function(event, ui) {
                            $("input#cuentaId").val(ui.item.id);
                            $("input#cuentaNombre").focus();
                            return false;
                        }
                    });
                    
                    $('input#codigo').focus();
                });
            </script>                    
        </content>
    </body>
</html>
