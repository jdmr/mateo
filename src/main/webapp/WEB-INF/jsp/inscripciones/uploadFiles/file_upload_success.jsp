<%-- 
    Document   : file_upload_success
    Created on : 29/05/2013, 03:09:06 PM
    Author     : develop
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<html>
    <head>
        <title>Spring MVC Multiple File Upload</title>
    </head>
    <body>
        <h1>Spring Multiple File Upload example</h1>
        <p>Following files are uploaded successfully.</p>
        <ol>
            <c:forEach items="${files}" var="file">
                <li>${file}</li>
                </c:forEach>
            <a  href="<s:url value='/inscripciones/uploadFiles/descargarPdf'/>"> Descargar PDF</a>
            <a  href="<s:url value='/inscripciones/uploadFiles/descargarXML'/>"> Descargar XML</a>


        </ol>
    </body>
</html>
