
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<h2>Storage Locations</h2>

<c:if test="${not empty successMessage}">
    <p style="color: green;">${successMessage}</p>
</c:if>

<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/storage-locations/new">
        Add New Storage Location
    </a>
</p>

<table border="1" cellpadding="10" cellspacing="0" width="100%">
    <thead>
        <tr>
            <th>ID</th>
            <th>Warehouse</th>
            <th>Location Code</th>
            <th>Location Type</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="location" items="${locations}">
            <tr>
                <td>${location.locationId}</td>
                <td>${location.warehouse.warehouseName}</td>
                <td>${location.locationCode}</td>
                <td>${location.locationType}</td>
                <td>
                    <c:choose>
                        <c:when test="${location.active}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/storage-locations/edit/${location.locationId}">
                        Edit
                    </a>

                    <form method="post"
                          action="${pageContext.request.contextPath}/storage-locations/delete/${location.locationId}"
                          style="display:inline;"
                          onsubmit="return confirm('Are you sure you want to delete this storage location?');">

                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}" />

                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty locations}">
            <tr>
                <td colspan="6">No storage locations found.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../fragments/footer.jsp" %>
