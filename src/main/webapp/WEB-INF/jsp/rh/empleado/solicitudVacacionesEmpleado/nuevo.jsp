<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="vacacionesEmpleado.nuevo.label" /></title>
    </head>
    <body>
        <br/>
        <br/>
        <br/>
        <br/>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="solicitudVacacionesEmpleado" />
        </jsp:include>

        <div id="nuevo-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="vacacionesEmpleado.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/vacacionesEmpleado'/>"><i class="icon-list icon-white"></i> <s:message code='vacacionesEmpleado.lista.label' /></a>
            </p>
            <form:form commandName="solicitudVacacionEmpleado" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="solicitudVacacionEmpleado.fechaInicio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaInicio">
                                <s:message code="fechaInicio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaInicio" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="fechaInicio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="solicitudVacacionEmpleado.fechaFinal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaFinal">
                                <s:message code="fechaFinal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaFinal" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="fechaFinal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="solicitudVacacionEmpleado.contactoTelefono">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="contactoTelefono">
                                <s:message code="telefonoCelular.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="contactoTelefono" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="contactoTelefono" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="solicitudVacacionEmpleado.contactoCorreo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="contactoCorreo">
                                <s:message code="correo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="contactoCorreo" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="contactoCorreo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="solicitudVacacionEmpleado.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:textarea path="observaciones" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="solicitudVacacionEmpleado.primaVacacional">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="primaVacacional">
                                <s:message code="primaVacacional.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="primaVacacional" value="true"  cssClass="span3" />
                            <form:errors path="primaVacacional" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="solicitudVacacionEmpleado.nacional">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nacional">
                                <s:message code="viajeNacional.label" />
                                <span class="required-indicator">*</span>
                            </label>

                            <form:radiobutton path="nacional" value="true"  cssClass="span1" />Si
                            <form:radiobutton path="nacional" value="false"  cssClass="span1"  onfocus="muestra_oculta('contenido_a_mostrar')" />No
                            <div id="contenido_a_mostrar">
                                <p>Recuerde! Al viajar fuera de México, Ud. tiene la obligación de tramitar un seguro de accidentes. 
                                </p><p> De lo contrario, la Universidad de Montemorelos queda libre de toda responsabilidad.</p>
                            </div>
                            <form:errors path="nacional" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="cresar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/vacacionesEmpleado'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
                $("input#fechaInicio").datepicker($.datepicker.regional['es']);
                $("input#fechaInicio").datepicker("option", "firstDay", 0);
                $("input#fechaFinal").datepicker($.datepicker.regional['es']);
                $("input#fechaFinal").datepicker("option", "firstDay", 0);
            });
            function muestra_oculta(id) {
                if (document.getElementById) { //se obtiene el id
                    var el = document.getElementById(id); //se define la variable "el" igual a nuestro div
                    el.style.display = (el.style.display == 'none') ? 'block' : 'none'; //damos un atributo display:none que oculta el div
                }
            }
            window.onload = function() {/*hace que se cargue la función lo que predetermina que div estará oculto hasta llamar a la función nuevamente*/
                muestra_oculta('contenido_a_mostrar');/* "contenido_a_mostrar" es el nombre que le dimos al DIV */
            }
        </script>                    
    </content>
</body>
</html>
