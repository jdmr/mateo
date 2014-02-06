<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en"><head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <title>Datos Empleado</title>

        <style type="text/css">
            /* Custom Styles */
            ul.nav-tabs{
                top: 100px;
                width: 160px;
                box-shadow: 0 1px 10px rgba(0, 0, 0, 0.067);
            }
            ul.nav-tabs li a{
                padding: 8px 24px;
            }
            ul.nav-tabs li.active a, ul.nav-tabs li.active a:hover{
                color: #fff;
                background: #0088cc;
                border: 1px solid #0088cc;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>

        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3" id="myScrollspy">
                    <ul class="nav nav-tabs nav-stacked affix">
                        <li class="brandab"><a href="#section1"><i class="icon-user "></i> Datos Personales</a></li>
                        <li><a href="#section2"><i class="icon-list-alt "></i> Datos Laborales</a></li>
                        <li><a href="<s:url value='/rh/empleado/claveEmpleado/nuevo'/>"><i class="icon-pencil"></i> Clave Empleado</a></li>
                        <li><a href="<s:url value='/rh/empleado/solicitudVacacionesEmpleado/nuevo'/>"><i class="icon-road "></i> Solicitud Vacaciones</a></li>
                        <li><a href="<s:url value='/rh/empleado/empleadoPerded'/>"><i class="icon-pencil"></i> Datos Per/Ded</a></li>
                        <li><a href="#section5"><i class="icon-list-alt "></i> </a></a></li>
                    </ul>
                </div>
                <div  class="span9">
                    <div id="section1">


                        <br/>
                        <br/>

                        <h2>Datos Personales de ${empleado.nombreCompleto}</h2>
                        <div class="btn-group">
                            <button  class="btn btn-info "><i class="icon-white icon-folder-close"></i>Info</button>
                            <button class="btn btn-info dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <li><a href="<s:url value='/rh/empleado/edita/${empleado.id}'/>"><i class=" icon-edit"></i>Editar Datos</a></li>
                                <li><a href="<s:url value='/rh/empleado/claveEmpleado/nuevo'/>"><i class="icon-certificate"></i> Cambiar Clave</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4> <s:message code="nombre.label" /></h4>
                                <h3>${empleado.nombre}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="apPaterno.label" /></h4>
                                <h3>${empleado.apPaterno}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="apMaterno.label" /></h4>
                                <h3>${empleado.apMaterno}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4> <s:message code="genero.label" /></h4>
                                <h3>${empleado.genero}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="estadoCivil.label" /></h4>
                                <h3>${empleado.estadoCivil}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="direccion.label" /></h4>
                                <h3>${empleado.direccion}</h3>
                            </div>

                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4> <s:message code="imms.label" /></h4>
                                <h3>${empleado.imms}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="ife.label" /></h4>
                                <h3>${empleado.ife}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="curp.label" /></h4>
                                <h3>${empleado.curp}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="madre.label" /></h4>
                                <h3>${empleado.madre}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="padre.label" /></h4>
                                <h3>${empleado.padre}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="conyuge.label" /></h4>
                                <h3>${empleado.conyuge}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="finadoMadre.label" /></h4>
                                <h3 ><form:checkbox path="empleado.finadoMadre" disabled="true" /></h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="finadoPadre.label" /></h4>
                                <h3 ><form:checkbox path="empleado.finadoPadre" disabled="true" /></h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="adventista.label" /></h4>
                                <h3 ><form:checkbox path="empleado.adventista" disabled="true" /></h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="iglesia.label" /></h4>
                                <h3>${empleado.iglesia}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="dias.label" /></h4>
                                <h3>${diasLibres}</h3>
                            </div>

                        </div>
                    </div>
                    <hr>
                    <div id="section2">
                        <br/>
                        <br/>
                        <br/>
                        <h2>Datos Laborales</h2>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="escalafon.label" /></h4>
                                <h3>${empleado.escalafon}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="turno.label" /></h4>
                                <h3>${empleado.turno}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="experienciaFueraUm.label" /></h4>
                                <h3>${empleado.experienciaFueraUm}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="responsabilidad.label" /></h4>
                                <h3>${empleado.escalafon}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="rango.label" /></h4>
                                <h3>${empleado.rango}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="modalidad.label" /></h4>
                                <h3>${empleado.modalidad}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="rfc.label" /></h4>
                                <h3>${empleado.rfc}</h3>
                            </div>

                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="cuenta.label" /></h4>
                                <h3>${empleado.cuenta}</h3>
                            </div>
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="status.label" /></h4>
                                <h3>${empleado.status}</h3>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-bottom: 10px;">
                            <div class="span3" style="padding-bottom: 10px;">
                                <h4><s:message code="clave.label" /></h4>
                                <h3>${empleado.claveActual}</h3>
                            </div>


                        </div>

                    </div>
                    <hr>
                    <div id="section3">

                    </div>
                    <hr>
                    <div id="section4">

                    </div>
                    <hr>
                    <div id="section5">
                    </div>
                </div>
            </div>
        </div>
    </body>
</html> 