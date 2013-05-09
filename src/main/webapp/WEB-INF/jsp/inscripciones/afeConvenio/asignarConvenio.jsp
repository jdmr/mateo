<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="afeConvenio.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="afeConvenio" />
        </jsp:include>

        <div id="nuevo-convenio" class="content scaffold-list" role="main">
            <h1><s:message code="afeConvenio.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/afeConvenio/asignarConvenio'/>"><i class="icon-list icon-white"></i> <s:message code='afeConvenio.lista.label' /></a>
            </p>
            <form:form commandName="afeConvenio" action="obtenerAlumno" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="afeConvenio.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="7" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>
                
                <c:if test="${afeConvenio.alumno.matricula != null}">
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${afeConvenio.alumno.nombre}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="apPaterno.label" /></div>
                    <div class="span11">${afeConvenio.alumno.apPaterno}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="apMaterno.label" /></div>
                    <div class="span11">${afeConvenio.alumno.apMaterno}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="estadoCivil.label" /></div>
                    <div class="span11">${afeConvenio.alumno.estadoCivil}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="genero.label" /></div>
                    <div class="span11">${afeConvenio.alumno.genero}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaNacimiento.label" /></div>
                    <div class="span11">${afeConvenio.alumno.fNacimiento}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="telefono.label" /></div>
                    <div class="span11">${afeConvenio.alumno.telefono}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="email.label" /></div>
                    <div class="span11">${afeConvenio.alumno.email}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="matricula.label" /></div>
                    <div class="span11">${afeConvenio.alumno.matricula}</div>
                </div>
                
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="guardar" onclick="graba();" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='guardar.button'/></button>
                </c:if>

                <p class="well" style="margin-top: 10px;">
                     <c:if test="${afeConvenio.alumno.matricula == null}">
                         <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="buscar" onclick="obtenerAlumno();" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='buscar.button'/></button>
                     </c:if>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/afeConvenio/asignarConvenio/'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
               
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#descripcion').focus();
            });
            
            function obtenerAlumno(){
                document.getElementById("afeConvenio").action=<s:url value='/inscripciones/afeConvenio/asignarConvenio/'/>
                document.getElementById("afeConvenio").submit();
                return true;
            }
            
            function graba(){
                document.getElementById("afeConvenio").action='graba';
                document.getElementById("afeConvenio").submit();
                return true;
            }
        </script>                    
    </content>
</body>
</html>
