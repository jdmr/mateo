<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="pedidoColportor.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="pedidoColportor" />
        </jsp:include>

        <div id="nuevo-pedidoColportor" class="content scaffold-list" role="main">
            <h1><s:message code="pedidoColportor.nuevo.label" /></h1>
            <h4>
            ${colportor.clave}
            ${colportor.nombreCompleto}
            </h4>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/pedidos'/>"><i class="icon-list icon-white"></i>
                    <s:message code='pedidoColportor.lista.label' /></a>
            </p>
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span9">
                    <div class="span6">
                        <c:if test="${colportor != null}">
                        <form action="obtieneProyecto" method="post">
                            <label for="proyecto">
                                <s:message code="proyecto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <input type="text" id="proyectoId" name="proyectoId" maxlength="15" required="true" value="${proyectoColportor.codigo}"/>
                            <button type="submit" name="buscarBtn" class="btn btn-primary btn-small" id="buscar" >
                                <i class="icon-search icon-white"></i>&nbsp;<s:message code='buscar.button'/></button>
                        </form>
                        <p class="well">
                        <s:message code="proyecto.label" /> ${proyectoColportor.nombre}<br>
                        <s:message code="descripcion.label" /> ${proyectoColportor.descripcion}<br>
                        <s:message code="fechaInicio.label" /> ${proyectoColportor.fechaInicio}
                        </p>
                        </c:if>
                    </div>
                        
                    <div class="span6">
                        <c:if test="${proyectoColportor != null}">
                        <form action="obtieneCliente" method="post">
                            <label for="cliente">
                                <s:message code="cliente.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <input type="text" id="clienteId" name="clienteId" maxlength="15" required="true" value="${clienteColportor.id}"/>
                            <button type="submit" name="crearBtn" class="btn btn-primary btn-small" id="crear" >
                                <i class="icon-search icon-white"></i>&nbsp;<s:message code='buscar.button'/></button>
                        </form>
                        <p class="well">
                        <s:message code="nombre.label" /> ${clienteColportor.nombreCompleto}<br>
                        <s:message code="direccion.label" /> ${clienteColportor.direccion1}<br>
                        <s:message code="email.label" /> ${clienteColportor.email}
                        </p>
                        </c:if>
                    </div>
                        </div>
                </div>
            </div>
            <c:if test="${clienteColportor != null and proyectoColportor != null}">
            <div class="row-fluid">
                <div class="span16">
            <div class="hero-unit"    >
            <form:form commandName="pedidoColportor" action="crea" method="post">
                <form:hidden path="cliente.id" value="${clienteColportor.id}"/>
                <form:hidden path="proyecto.id" value="${proyectoColportor.id}"/>
                
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <div class="span6">
                    <s:bind path="pedidoColportor.lugar">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="lugar">
                                <s:message code="lugar.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="lugar" maxlength="120" required="true" class="span6"/>
                            <form:errors path="lugar" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportor.fechaPedido">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaPedido">
                                <s:message code="fechaPedido.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaPedido" maxlength="15" required="true" class="span6"/>
                            <form:errors path="fechaPedido" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportor.horaPedido">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="horaPedido">
                                <s:message code="horaPedido.label" />
                            </label>
                            <form:input path="horaPedido" maxlength="15"  class="span6"/>
                            <form:errors path="horaPedido" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    </div>
                    <div class="span6">
                    <s:bind path="pedidoColportor.fechaEntrega">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaEntrega">
                                <s:message code="fechaEntrega.label" />
                            </label>
                            <form:input path="fechaEntrega" maxlength="15"  class="span6"/>
                            <form:errors path="fechaEntrega" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportor.horaEntrega">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="horaEntrega">
                                <s:message code="horaEntrega.label" />
                            </label>
                            <form:input path="horaEntrega" maxlength="15"  class="span6"/>
                            <form:errors path="horaEntrega" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportor.formaPago">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="formaPago">
                                <s:message code="formaPago.label" />
                            </label>
                            <form:select id="formaPago" path="formaPago" required="true" class="span6" >
                                <form:options items="${formasPago}" itemValue="name" itemLabel="value" />
                            </form:select>
                            <form:errors path="formaPago" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportor.razonSocial">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="razonSocial">
                                <s:message code="razonSocial.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="razonSocial" maxlength="120" required="true" class="span6"/>
                            <form:errors path="razonSocial" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    </div>
                    <s:bind path="pedidoColportor.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                            </label>
                            <form:input path="observaciones" maxlength="250" class="span12" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportaje/ventas/pedidos'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
            </div>
            </div>
            </div>
            </c:if>
        </div>
        <content>
        <script>
            $(function() {
            
            $( "#clienteId" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/ventas/clientes/get_cliente_list',
                select: function(event, ui) {
                        $("input#clienteId").val(ui.item.id);
                        
                        return false;
                    }
            })
            });
            </script>
        <script>
            $(document).ready(function() {
                $('input#proyecto.id').focus();
                $("input#fechaPedido").datepicker($.datepicker.regional['es']);
                $("input#fechaPedido").datepicker("option", "firstDay", 0);
                $("input#fechaEntrega").datepicker($.datepicker.regional['es']);
                $("input#fechaEntrega").datepicker("option", "firstDay", 0);
            });

           
        </script>
        <script>
            $(function() {
            
            $( "#proyectoId" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/ventas/proyectoColportor/get_proyecto_list',
                select: function(event, ui) {
                        $("input#proyectoId").val(ui.item.nombre);
                        
                        return false;
                    }
            })
            });
            </script>
        <script>
            $(function() {
            
            $( "#clave" ).autocomplete({
                source: '${pageContext. request. contextPath}/colportaje/colportor/get_colportor_list',
                select: function(event, ui) {
                        $("input#clave").val(ui.item.nombre);
                        
                        return false;
                    }
            })
            });
          </script>
    </content>
    </body>
</html>
