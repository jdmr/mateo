<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="datosEmpleado.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>


        <div id="ver-empleado" class="content scaffold-list" role="main">
            <h1><s:message code="datosEmpleado.label" /></h1>

            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/empleado/elimina" />
            <form:form commandName="empleado" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1">
                        <a  href="#Generales">
                            <div align="absmiddle " >
                                <img  width="25"src="<c:url value='/images/vista/Edit file.png' />"  alt="Datos Generales" />
                            </div>
                        </a>              
                    </div>
                    <div class="span1">
                        <a  href="#Personales">
                            <div align="absmiddle " >
                                <img  width="25"src="<c:url value='/images/vista/User.png' />"  alt="Datos Personales" />
                            </div>
                        </a>
                    </div>
                    <div class="span1">
                        <a  href="#Personales">
                            <div align="absmiddle " >
                                <img  width="25"src="<c:url value='/images/vista/Help and Support.png' />"  alt="Datos Laborales" />
                            </div>
                        </a>
                    </div>
                </div>

                <a id="Generales">
                    <div align="absmiddle " >
                        <img  width="75"src="<c:url value='/images/vista/Edit file.png' />" />
                    </div>
                </a> 
                <h4>Datos Generales</h4>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="clave.label" /></h4>
                        <h4>${empleado.clave}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="nombreCompleto.label" /></h4>
                        <h4>${empleado.nombre}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="apPaterno.label" /></h4>
                        <h4>${empleado.apPaterno}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="apMaterno.label" /></h4>
                        <h4>${empleado.apMaterno}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="genero.label" /></h4>
                        <h4>${empleado.genero}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="fechaNacimiento.label" /></h4>
                        <h4>
                            <fmt:formatDate dateStyle="long" value="${empleado.fechaNacimiento}"/>
                        </h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="curp.label" /></h4>
                        <h4>${empleado.curp}</h4>
                    </div>

                    <div class="span3">
                        <h4><s:message code="ife.label" /></h4>
                        <h4>${empleado.ife}</h4>
                    </div> 
                    <div class="span3">
                        <h4><s:message code="direccion.label" /></h4>
                        <h4>${empleado.direccion}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="estadoCivil.label" /></h4>
                        <h4>${empleado.estadoCivil}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="adventista.label" /></h4>
                        <div class="span13"><form:checkbox path="adventista" disabled="true" /></div>
                    </div>
                </div>

                <a id="Personales">
                    <div align="absmiddle " >
                        <img  width="75"src="<c:url value='/images/vista/User.png' />" class="img-circle"/>
                    </div>
                </a> 
                <h4>Datos Personales</h4>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="conyuge.label" /></h4>
                        <h4>${empleado.conyuge}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="madre.label" /></h4>
                        <h4>${empleado.madre}</h4>
                    </div>

                    <div class="span3">
                        <h4><s:message code="padre.label" /></h4>
                        <h4>${empleado.padre}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="fechaMatrimonio.label" /> </h4>
                        <h4>
                            <fmt:formatDate dateStyle="long" value="${empleado.fechaMatrimonio}"/>
                        </h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="finadoPadre.label" /></h4>
                        <div class="span13"><form:checkbox path="finadoPadre" disabled="true" /></div>
                    </div>

                    <div class="span3">
                        <h4><s:message code="finadoMadre.label" /></h4>
                        <div class="span13"><form:checkbox path="finadoMadre" disabled="true" /></div>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="iglesia.label" /></h4>
                        <h4>${empleado.iglesia}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="nivelEstudios.label" /></h4>
                        <h4>${empleado.nivelEstudios}</h4>
                    </div>
                </div>

                <a id="Personales">
                    <div align="absmiddle " >
                        <img  width="75"src="<c:url value='/images/vista/Help and Support.png' />" class="img-circle"/>
                    </div>
                </a> 
                <h4>Datos Laborales</h4>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="status.label" /></h4>
                        <h4>${empleado.status}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="turno.label" /></h4>
                        <h4>${empleado.turno}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="rango.label" /></h4>
                        <h4>${empleado.rango}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="escalafon.label" /></h4>
                        <h4>${empleado.escalafon}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="modalidad.label" /></h4>
                        <h4>${empleado.modalidad}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="responsabilidad.label" /></h4>
                        <h4>${empleado.responsabilidad}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="cuenta.label" /></h4>
                        <h4>${empleado.cuenta}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="imms.label" /></h4>
                        <h4>${empleado.imms}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="rfc.label" /></h4>
                        <h4>${empleado.rfc}</h4>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span3">
                        <h4><s:message code="experienciaFueraUm.label" /></h4>
                        <h4>${empleado.experienciaFueraUm}</h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="fechaAlta.label" /></h4>
                        <h4>
                            <fmt:formatDate dateStyle="long" value="${empleado.fechaAlta}"/>
                        </h4>
                    </div>
                    <div class="span3">
                        <h4><s:message code="fechaBaja.label" /></h4>
                        <h4>
                            <fmt:formatDate dateStyle="long" value="${empleado.fechaBaja}"/>
                        </h4>
                    </div>
                </div>

                <p><a href="#">Regresar al Inicio</a></p>
            </form:form>
        </div>
    </body>
</html>

