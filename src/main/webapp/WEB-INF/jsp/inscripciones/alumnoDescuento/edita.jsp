<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="alumnoDescuento.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="alumnoDescuento" />
        </jsp:include>

        <div id="edita-alumnoDescuento" class="content scaffold-list" role="main">
            <h1><s:message code="alumnoDescuento.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/alumnoDescuento'/>"><i class="icon-list icon-white"></i> <s:message code='alumnoDescuento.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/alumnoDescuento/graba" />
            <form:form commandName="alumnoDescuento" method="post" action="${actualizaUrl}">
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
                <form:hidden path="status" />

                <fieldset>
                    <s:bind path="alumnoDescuento.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="7" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="alumnoDescuento.descuento">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="descuento">
                                <s:message code="descuento.label" />
                                <span class="required-indicator">*</span>
                                </label>
                                <form:select id="descuento" path="descuento.id" required="true" cssClass="span3" >
                                    <form:options items= "${descuentos}" itemValue="id" itemLabel="descripcion"/>
                                </form:select>
                                <form:errors path="descuento" cssClass="alert alert-error" />
                                     
                        </div>
                    </s:bind>
                    <s:bind path="alumnoDescuento.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="150" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="alumnoDescuento.contabiliza">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="contabiliza">
                                <s:message code="contabiliza.label" />
                                <span class="required-indicator">*</span>
                            </label>
                                <form:radiobutton path="contabiliza"  value="1"  cssClass="span3" />Si<br />
                                <form:radiobutton path="contabiliza"  value="0"  cssClass="span3" />No<br />
                            <form:errors path="contabiliza" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/alumnoDescuento/ver/${alumnoDescuento.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
