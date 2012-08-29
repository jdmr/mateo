<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleado.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>

       <div id="edita-empleado" class="content scaffold-list" role="main">
            <h1><s:message code="empleado.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleado'/>"><i class="icon-list icon-white"></i> <s:message code='empleado.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/empleado/actualiza" />
            <form:form commandName="empleado" method="post" action="${actualizaUrl}">
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

                <fieldset>
                    <s:bind path="empleado.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.apPaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apPaterno">
                                <s:message code="apPaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apPaterno" maxlength="128" required="true" />
                            <form:errors path="apPaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.apMaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apMaterno">
                                <s:message code="apMaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apMaterno" maxlength="128" required="true" />
                            <form:errors path="apMaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="128" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.genero">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="genero">
                                <s:message code="genero.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="genero" maxlength="128" required="true" />
                            <form:errors path="genero" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.direccion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="direccion">
                                <s:message code="direccion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="direccion" maxlength="128" required="true" />
                            <form:errors path="direccion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.curp">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="curp">
                                <s:message code="curp.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="curp" maxlength="128" required="true" />
                            <form:errors path="curp" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.rfc">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="rfc">
                                <s:message code="rfc.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="rfc" maxlength="128" required="true" />
                            <form:errors path="rfc" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.cuenta">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="cuenta">
                                <s:message code="cuenta.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="cuenta" maxlength="128" required="true" />
                            <form:errors path="cuenta" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.imms">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="imms">
                                <s:message code="imms.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="imms" maxlength="128" required="true" />
                            <form:errors path="imms" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.ife">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="ife">
                                <s:message code="ife.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="ife" maxlength="128" required="true" />
                            <form:errors path="ife" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="128" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.modalidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="modalidad">
                                <s:message code="modalidad.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="modalidad" maxlength="128" required="true" />
                            <form:errors path="modalidad" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.rango">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="rango">
                                <s:message code="rango.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="rango" maxlength="128" required="true" />
                            <form:errors path="rango" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.escalafon">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="escalafon">
                                <s:message code="escalafon.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="escalafon" maxlength="128" required="true" />
                            <form:errors path="escalafon" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.turno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="turno">
                                <s:message code="turno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="turno" maxlength="128" required="true" />
                            <form:errors path="turno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.experienciaFueraUm">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="experienciaFueraUm">
                                <s:message code="experienciaFueraUm.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="experienciaFueraUm" maxlength="128" required="true" />
                            <form:errors path="experienciaFueraUm" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.responsabilidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="resposabilidad">
                                <s:message code="responsabilidad.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="responsabilidad" maxlength="128" required="true" />
                            <form:errors path="responsabilidad" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.estadoCivil">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="estadoCivil">
                                <s:message code="estadoCivil.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="estadoCivil" maxlength="128" required="true" />
                            <form:errors path="estadoCivil" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.conyuge">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="conyuge">
                                <s:message code="conyuge.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="conyuge" maxlength="128" required="true" />
                            <form:errors path="conyuge" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.padre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="padre">
                                <s:message code="padre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="padre" maxlength="128" required="true" />
                            <form:errors path="padre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.madre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="madre">
                                <s:message code="madre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="madre" maxlength="128" required="true" />
                            <form:errors path="madre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.finadoMadre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fiandoMadre">
                                <s:message code="finadoMadre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="finadoMadre" cssClass="span3" />
                            <form:errors path="finadoMadre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.finadoPadre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fiandoPadre">
                                <s:message code="finadoPadre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="finadoMadre" cssClass="span3" />
                            <form:errors path="finadoMadre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>  
                    <s:bind path="empleado.adventista">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="adventista">
                                <s:message code="adventista.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="adventista" cssClass="span3" />
                            <form:errors path="adventista" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.iglesia">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="iglesia">
                                <s:message code="iglesia.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="iglesia" maxlength="128" required="true" />
                            <form:errors path="iglesia" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/empleado/ver/${empleado.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
