<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="uploadFile.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="main" />
        </jsp:include>

        <div id="nuevo-producto" class="content scaffold-list" role="main">
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-error fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <strong><s:message code="${message}" arguments="${messageAttrs}" /></strong>
                </div>
            </c:if>


            <form:form commandName="informeProveedorDetalle" action="uploadFile" method="post" enctype="multipart/form-data">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <c:if test="${esPdf}">
                    <h1><s:message code="subirPDF.label" /></h1>
                    <img src="<c:url value='/images/pdf.png'/>" width="120" height="100" />
                </c:if>
                <c:if test="${esXml}">
                    <h1><s:message code="subirXML.label" /></h1>
                    <img src="<c:url value='/images/xml.png'/>" width="120" height="100" />
                </c:if>
                <fieldset>

                    <div class="row-fluid">
                        <div class="span4">
                            <div class="control-group">
                                <label for="file">
                                    <s:message code="name.label" />
                                </label>
                                <input name="file" type="file" />
                            </div>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='upload.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/factura/informeProveedorDetalle'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#codigo').focus();
            });
        </script>                    
    </content>
</body>
</html>
