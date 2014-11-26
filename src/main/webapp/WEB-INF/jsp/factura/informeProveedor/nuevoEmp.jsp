<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeProveedor.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedor" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="informeProveedor.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/informeProveedor'/>"><i class="icon-list icon-white"></i> <s:message code='informeProveedor.lista.label' /></a>
            </p>
            <form:form commandName="informeProveedor"  method="post" action="${flowExecutionUrl}">
                 <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>

                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>


                    <s:bind path="informeProveedor.fechaInforme">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaInforme">
                                <s:message code="fechaInforme.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaInforme" maxlength="12" required="true" />
                            <form:errors path="fechaInforme" cssClass="alert alert-error" />
                        </div>
                    </s:bind>


                    <s:bind path="informeProveedor.banco">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="banco">
                                <s:message code="banco.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="banco" size="18" maxlength="25" required="true" />
                            <form:errors path="banco" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                   
                    <s:bind path="informeProveedor.moneda">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="moneda">
                                <s:message code="moneda.label" />

                            </label>
                            <form:radiobutton path="moneda"  value="P" cssClass="span3" id="pesos"/><s:message code="pesos.label"/><br />
                            <form:radiobutton path="moneda"  value="D"  cssClass="span3" id="dolares"/><s:message code="dolares.label"/><br />
                            <form:errors path="moneda" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.tipoDocumento">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoDocumento">
                                <s:message code="tipoDocumento.label" />

                            </label>
                            <form:radiobutton path="tipoDocumento"  value="I" cssClass="span3" id="informe"/><s:message code="informe.label"/><br />
                            <form:radiobutton path="tipoDocumento"  value="R"  cssClass="span3" id="reembolso"/><s:message code="reembolso.label"/><br />
                            <form:errors path="tipoDocumento" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                
                    
                 

                </fieldset>


                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="_eventId_grabaInforme" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="${flowExecutionUrl}&_eventId=endState"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
                $("input#fechaInforme").datepicker($.datepicker.regional['es']);
                $("input#fechaInforme").datepicker("option", "firstDay", 0);
            });
//            function disableCuenta()
//            {
//                document.getElementById("cuenta").disabled = true;
//                document.getElementById("clabe").disabled = false;
//
//            }
//            function disableClave()
//            {
//                document.getElementById("clabe").disabled = true;
//                document.getElementById("cuenta").disabled = false;
//
//            }
        </script>
    </content>
</body>
</html>
