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
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-heading fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <strong><s:message code="${message}" arguments="${messageAttrs}" /></strong>
                </div>
            </c:if>

            <form:form commandName="informeProveedor"  method="post" action="graba">
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

                    <s:bind path="informeProveedor.formaPago">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="formaPago">
                                <s:message code="formaPago.label" />
                                <span class="required-indicator">*</span>

                            </label>
                            <form:radiobutton path="formaPago"  value="T" cssClass="span3" id="tranferencia" />Transferencia<br />
                            <form:radiobutton path="formaPago"  value="C"  cssClass="span3" id="cheque"/>Cheque<br />
                            <form:errors path="formaPago" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.clabe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clabe">
                                <s:message code="clabe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clabe" size="18" maxlength="25" />
                            <form:errors path="clabe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.cuentaCheque">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="cuentaCheque">
                                <s:message code="cuentaCheque.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="cuentaCheque" size="18" maxlength="25"  />
                            <form:errors path="cuentaCheque" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.moneda">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="moneda">
                                <s:message code="moneda.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:radiobutton path="moneda"  value="P" cssClass="span3" id="pesos"/><s:message code="pesos.label"/><br />
                            <form:radiobutton path="moneda"  value="D"  cssClass="span3" id="dolares"/><s:message code="dolares.label"/><br />
                            <form:errors path="moneda" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <%--
                    <s:bind path="informeProveedor.contabilidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="cliente">
                                <s:message code="cliente.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="contabilidad"   maxlength="12" required="true" />
                            <form:errors path="contabilidad" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    --%>
                    <s:bind path="informeProveedor.contabilidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="cliente">
                                <s:message code="cliente.label" />
                                <span class="required-indicator">*</span>
                                <select name="contabilidad">  
                                    <option value="1" selected>Universidad de Montemorelos</option>  
                                    <option value="2">COVOPROM</option>  
                                    <option value="5">Patronato</option>  
                                </select> 
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.ccp">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="ccp">
                                <s:message code="ccp.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="ccp"   maxlength="12"  />
                            <form:errors path="ccp" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>


                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/factura/informeProveedor'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
