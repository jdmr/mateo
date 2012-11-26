<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleado.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>

        
        <div id="ver-empleado" class="content scaffold-list" role="main">
            <h1><s:message code="empleado.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleado'/>"><i class="icon-list icon-white"></i> <s:message code='empleado.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/empleado/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='empleado.nuevo.label' /></a>
            </p>
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
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${empleado.nivelEstudios}</div>
                </div>
                
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${empleado.nombre}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="apPaterno.label" /></div>
                    <div class="span11">${empleado.apPaterno}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="apMaterno.label" /></div>
                    <div class="span11">${empleado.apMaterno}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="clave.label" /></div>
                    <div class="span11">${empleado.clave}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="genero.label" /></div>
                    <div class="span11">${empleado.genero}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="direccion.label" /></div>
                    <div class="span11">${empleado.direccion}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="status.label" /></div>
                    <div class="span11">${empleado.status}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="curp.label" /></div>
                    <div class="span11">${empleado.curp}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="modalidad.label" /></div>
                    <div class="span11">${empleado.modalidad}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="rfc.label" /></div>
                    <div class="span11">${empleado.rfc}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="cuenta.label" /></div>
                    <div class="span11">${empleado.cuenta}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="imms.label" /></div>
                    <div class="span11">${empleado.imms}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="ife.label" /></div>
                    <div class="span11">${empleado.ife}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="rango.label" /></div>
                    <div class="span11">${empleado.rango}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="madre.label" /></div>
                    <div class="span11">${empleado.madre}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="padre.label" /></div>
                    <div class="span11">${empleado.padre}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="estadoCivil.label" /></div>
                    <div class="span11">${empleado.estadoCivil}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="conyuge.label" /></div>
                    <div class="span11">${empleado.conyuge}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="iglesia.label" /></div>
                    <div class="span11">${empleado.iglesia}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="responsabilidad.label" /></div>
                    <div class="span11">${empleado.responsabilidad}</div>
                </div>
                   <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="finadoPadre.label" /></div>
                    <div class="span11"><form:checkbox path="finadoPadre" disabled="true" /></div>
                </div>
                   <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="finadoMadre.label" /></div>
                    <div class="span11"><form:checkbox path="finadoMadre" disabled="true" /></div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="adventista.label" /></div>
                    <div class="span11"><form:checkbox path="adventista" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="escalafon.label" /></div>
                    <div class="span11">${empleado.escalafon}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="turno.label" /></div>
                    <div class="span11">${empleado.turno}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="experienciaFueraUm.label" /></div>
                    <div class="span11">${empleado.experienciaFueraUm}</div>
                </div>
                <p class="well">
                    <a href="<c:url value='/rh/empleado/edita/${empleado.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <a href="<c:url value='/rh/empleado/datos/${empleado.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="datos.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>

