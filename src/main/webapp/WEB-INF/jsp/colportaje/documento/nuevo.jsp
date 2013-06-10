<%-- 
    Document   : nuevo
    Created on : 4/04/2012, 09:51:33 AM
    Author     : wilbert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="documento.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="documento" />
        </jsp:include>

        <div id="nuevo-documento" class="content scaffold-list" role="main">
            <h1><s:message code="documento.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../documento'/>"><i class="icon-list icon-white"></i> <s:message code='documento.lista.label' /></a>
            </p>
            <form:form commandName="documento" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="folio">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="documento.tipoDeDocumento">
                        <div class="control-group <c:if test='${not empty folio.errorMessages}'>error</c:if>">
                            <label for="tipoDeDocumento">
                                <s:message code="tipoDeDocumento.label" />
                                <span class="required-indicator">*</span>
                            </label>
                     <form:select path="TipoDeDocumento">
                                    <form:option value="0" label="Deposito_Caja" />
                                    <form:option value="1" label="Deposito_Banco" />
                                     <form:option value="2" label="Diezmo" />
                                    <form:option value="3" label="Notas_De_Compra" />
                                     <form:option value="4" label="Boletín" />
                                    <form:option value="5" label="Informe" />
                                </form:select>
                        </div>
                    </s:bind>
                         <s:bind path="documento.fecha">
                        <div class="control-group <c:if test='${not empty folio.errorMessages}'>error</c:if>">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="10" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                        <s:bind path="documento.folio">
                        <div class="control-group <c:if test='${not empty folio.errorMessages}'>error</c:if>">
                            <label for="folio">
                                <s:message code="folio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="folio" maxlength="30" required="true" />
                            <form:errors path="folio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                      <s:bind path="documento.importe">
                        <div class="control-group <c:if test='${not empty folio.errorMessages}'>error</c:if>">
                            <label for="importe">
                                <s:message code="importe.label" />
                                  <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importe" maxlength="20" required="true" />
                            <form:errors path="importe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                    
                      <s:bind path="documento.observaciones">
                        <div class="control-group <c:if test='${not empty folio.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                              
                            </label>
                            <form:textarea path="observaciones" maxlength="1000" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
          
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="_action_crea" class="btn btn-primary btn-large" value="<s:message code='crear.button'/>" id="crea" />
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#tipoDeDocumento').focus();
                });
            </script>                    
        </content>
    </body>
</html>
