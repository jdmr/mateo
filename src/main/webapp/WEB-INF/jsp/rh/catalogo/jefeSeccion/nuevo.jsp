<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="jefeSeccion.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="jefeSeccion" />
        </jsp:include>

        <div id="nuevo-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="jefeSeccion.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/jefeSeccion'/>"><i class="icon-list icon-white"></i> <s:message code='jefeSeccion.lista.label' /></a>
            </p>
            <form:form commandName="jefeSeccion" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>

                    <s:bind path="jefeSeccion.jefeSeccion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="jefeSeccion">
                                <s:message code="jefe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input id="nombreJefe" path="jefeSeccion.nombre" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="jefeSeccion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <table id="lista" class="table table-striped table-hover">
                        <thead>
                            <tr>

                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="agregar" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="nombre" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                                    <jsp:param name="columna" value="nombre" />
                                </jsp:include>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${jefes}" var="jefe" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td ><input type="checkbox" value="<c:out value="${jefe.id}"/>" name="checkJefeid-<c:out value="${jefe.id}"/>" id="checkJefeid" /></td>
                                    <td>${jefe.id}</td>                            
                                    <td>${jefe.jefe.nombre}</td>
                                </tr>
                            </c:forEach> 
                        </tbody>
                    </table>

                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="cresar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/jefeSeccion '/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombreJefe').focus();
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option", "firstDay", 0);
            });
        </script>                    
    </content>
</body>
</html>