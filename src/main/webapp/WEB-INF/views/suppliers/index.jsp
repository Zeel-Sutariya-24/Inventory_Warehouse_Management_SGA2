
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Suppliers</h1>

    <c:if test="${not empty successMessage}">
        <p style="color:green;">
            <c:out value="${successMessage}"/>
        </p>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <p style="color:red;">
            <c:out value="${errorMessage}"/>
        </p>
    </c:if>

    <p>
        <a href="${pageContext.request.contextPath}/suppliers/new">
            + Add New Supplier
        </a>
    </p>

    <table border="1" cellpadding="10" cellspacing="0">

        <thead>
            <tr>
                <th>ID</th>
                <th>Supplier Name</th>
                <th>Contact Person</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Address</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="supplier" items="${suppliers}">
                <tr>
                    <td>${supplier.supplierId}</td>
                    <td><c:out value="${supplier.supplierName}"/></td>
                    <td><c:out value="${supplier.contactName}"/></td>
                    <td><c:out value="${supplier.phone}"/></td>
                    <td><c:out value="${supplier.email}"/></td>
                    <td><c:out value="${supplier.address}"/></td>

                    <td>
                        <c:choose>
                            <c:when test="${supplier.active}">Active</c:when>
                            <c:otherwise>Inactive</c:otherwise>
                        </c:choose>
                    </td>

                    <td>
                        <a href="${pageContext.request.contextPath}/suppliers/edit/${supplier.supplierId}">
                            Edit
                        </a>

                        <form action="${pageContext.request.contextPath}/suppliers/delete/${supplier.supplierId}"
                              method="POST"
                              style="display:inline;"
                              onsubmit="return confirm('Are you sure you want to delete this supplier?');">

                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty suppliers}">
                <tr>
                    <td colspan="8">No suppliers found.</td>
                </tr>
            </c:if>
        </tbody>

    </table>

</main>

<%@ include file="../fragments/footer.jsp" %>
