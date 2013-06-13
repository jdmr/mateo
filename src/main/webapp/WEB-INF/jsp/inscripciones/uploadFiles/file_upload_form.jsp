<%-- 
    Document   : file_upload_form
    Created on : 29/05/2013, 12:43:39 PM
    Author     : develop
--%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Spring MVC Multiple File Upload</title>
        <script
        src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script>
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
    </head>
    <body>
        <h1>Spring Multiple File Upload example</h1>

        <form:form method="post" action="save.html"
                   modelAttribute="uploadForm" enctype="multipart/form-data">

            <p>Select files to upload. Press Add button to add more file inputs.</p>

            <input id="addFile" type="button" value="Add File" />
            <table id="fileTable">
                <tr>
                <p>XML*<p/>
                <td><img src="/mateo/images/xml.png" width="120" height="100" /><input name="files[0]" type="file" /></td>
            </tr>

            <tr>
            <p>PDF*<p/>
            <td><img src="/mateo/images/pdf.png" width="120" height="100" /><input name="files[1]" type="file" /></td>
        </tr>
    </table>
    <br/><input type="submit" value="Upload" />
</form:form>
</body>
</html>