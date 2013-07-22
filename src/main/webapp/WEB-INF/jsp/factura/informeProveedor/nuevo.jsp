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
            <form:form commandName="informeProveedor" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="informeProveedor.nombreProveedor">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nombreProveedor">
                                <s:message code="proveedor.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreProveedor" maxlength="50" required="true" />
                            <form:errors path="nombreProveedor" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="150" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
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
                    <s:bind path="informeProveedor.clabe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clabe">
                                <s:message code="clabe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clabe" maxlength="12" required="true" />
                            <form:errors path="clabe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedor.cuentaCheque">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="cuentaCheque">
                                <s:message code="cuentaCheque.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="cuentaCheque" maxlength="12" required="true" />
                            <form:errors path="cuentaCheque" cssClass="alert alert-error" />
                        </div>
                    </s:bind>


                    <s:bind path="informeProveedor.formaPago">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="formaPago">
                                <s:message code="formaPago.label" />

                            </label>
                            <form:radiobutton path="formaPago"  value="T" cssClass="span3" id="tranferencia"/>Transferencia<br />
                            <form:radiobutton path="formaPago"  value="C"  cssClass="span3" id="cheque"/>Cheque<br />
                            <form:errors path="formaPago" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>
                <%--
                <div class="control-group ">
                        <label for="clabe">
                            <s:message code="clabe.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <input type="text"   name="clabe" <c:out value='${informeProveedor.clabe}' />>
                    </div>
                    <div class="control-group ">
                        <label for="cuenta">
                            <s:message code="cuenta.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <input type="text"   name="cuenta" <c:out value='${cuentaCheque}' />>
                    </div>
        <s:bind path="proveedorFacturasId.cuenta">
            <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                    <label for="cuenta">
                    <s:message code="cuenta.label" />
                    <span class="required-indicator">*</span>
                </label>
                <form:input path="cuenta" maxlength="150" required="true" />
                <form:errors path="cuenta" cssClass="alert alert-error" />
            </div>
        </s:bind>
                
                <div class="control-group ">
                        <label for="clabe">
                            <s:message code="clabe.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <input type="text"   name="clabe" <c:out value='${informeProveedor.proveedorFacturas.clabe}' />>
                    </div>
                    <div class="control-group ">
                        <label for="cuenta">
                            <s:message code="cuenta.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <input type="text"   name="cuenta" <c:out value='${cuentaCheque}' />>
                    </div>
                <s:bind path="informeProveedor.proveedorFacturas.clabe">
                    <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clabe">
                            <s:message code="clabe.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="proveedorFacturas.clabe" maxlength="12" required="true" />
                        <form:errors path="proveedorFacturas.clabe" cssClass="alert alert-error" />
                    </div>
                </s:bind>

                --%>

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
