
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../fragments/header.jsp" %>

<h2>Edit Storage Location</h2>

<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>

<form:form method="post"
           action="${pageContext.request.contextPath}/storage-locations/update/${locationId}"
           modelAttribute="locationForm">

    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}" />

    <p>
        <label>Warehouse:</label><br>
        <form:select path="warehouseId">
            <form:option value="" label="-- Select Warehouse --"/>
            <form:options items="${warehouses}"
                          itemValue="warehouseId"
                          itemLabel="warehouseName"/>
        </form:select>
        <br>
        <form:errors path="warehouseId" cssStyle="color:red"/>
    </p>

    <p>
        <label>Location Code:</label><br>
        <form:input path="locationCode" maxlength="30"/>
        <br>
        <form:errors path="locationCode" cssStyle="color:red"/>
    </p>

    <p>
        <label>Location Type:</label><br>
        <form:select path="locationType">
            <form:options items="${locationTypes}"/>
        </form:select>
        <br>
        <form:errors path="locationType" cssStyle="color:red"/>
    </p>

    <p>
        <label>
            <form:checkbox path="active"/>
            Active
        </label>
    </p>

    <button type="submit">Update Location</button>
    <a href="${pageContext.request.contextPath}/storage-locations">Cancel</a>

</form:form>

<%@ include file="../fragments/footer.jsp" %>
