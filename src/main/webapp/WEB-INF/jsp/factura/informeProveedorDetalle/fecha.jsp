<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="fechaCompromiso.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedorDetalle" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="fechaCompromiso.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/informeProveedorDetalle/contrarecibos'/>"><i class="icon-list icon-white"></i> <s:message code='informeProveedorDetalle.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/factura/informeProveedorDetalle/actualizaFecha" />
            <form:form commandName="contrarecibo" method="post" action="${actualizaUrl}" >
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

                    <s:bind path="contrarecibo.fechaPago">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaPago">
                                <s:message code="fechaCompromiso.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaPago" maxlength="12" required="true" />
                            <form:errors path="fechaPago" cssClass="alert alert-error" />
                        </div>
                    </s:bind>



                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/factura/informeProveedorDetalle/revisar'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <%--
        <script
    src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script> --%>
        <script>
            $(document).ready(function () {
                $('input#nombre').focus();
                $("input#fechaPago").datepicker($.datepicker.regional['es']);
                $("input#fechaPago").datepicker("option", "firstDay", 0);
            });

            $(document).ready(function () {
                //add more file components if Add is clicked
                $('#addFile').click(function () {
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
