<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="reciboColportor.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="reciboColportor" />
        </jsp:include>

        <div id="edita-reciboColportor" class="content scaffold-list" role="main">
            <h1><s:message code="reciboColportor.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/recibos'/>"><i class="icon-list icon-white"></i>
                    <s:message code='reciboColportor.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/colportaje/ventas/recibos/actualiza" />
            <form:form commandName="reciboColportor" method="post" action="${actualizaUrl}">
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
                    <s:bind path="reciboColportor.numRecibo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="numRecibo">
                                <s:message code="numRecibo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="numRecibo" maxlength="15" required="true" />
                            <form:errors path="numRecibo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="reciboColportor.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="120" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="reciboColportor.importe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="importe">
                                <s:message code="importe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importe" maxlength="15" required="true" />
                            <form:errors path="importe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="reciboColportor.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                            </label>
                            <form:input path="observaciones" maxlength="15"  />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportaje/ventas/recibos/ver/${reciboColportor.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
        <%--
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script> --%>
        <script>
            $(document).ready(function() {
                $('input#numPedido').focus();
                $("input#fechaPedido").datepicker($.datepicker.regional['es']);
                $("input#fechaPedido").datepicker("option", "firstDay", 0);
                $("input#fechaEntrega").datepicker($.datepicker.regional['es']);
                $("input#fechaEntrega").datepicker("option", "firstDay", 0);
            });

            $(document).ready(function() {
                //add more file components if Add is clicked
                $('#addFile').click(function() {
                    var fileIndex = $('#fileTable tr').children().length - 1;
                    $('#fileTable').append(
                            '<tr><td>' +
                            '   <input type="file" name="files[' + fileIndex + ']" />' +
                            '</td></tr>');
                });

            });
        </script>

    </content>
    </body>
</html>
