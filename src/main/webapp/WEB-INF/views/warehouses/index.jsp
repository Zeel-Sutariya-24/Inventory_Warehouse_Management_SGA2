
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Warehouses</h1>

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
        <a href="${pageContext.request.contextPath}/warehouses/new">
            + Add New Warehouse
        </a>
    </p>

    <table border="1" cellpadding="10" cellspacing="0">

        <thead>
            <tr>
                <th>ID</th>
                <th>Warehouse Name</th>
                <th>Address</th>
                <th>Manager</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="warehouse" items="${warehouses}">
                <tr>
                    <td>${warehouse.warehouseId}</td>

                    <td>
                        <c:out value="${warehouse.warehouseName}"/>
                    </td>

                    <td>
                        <c:out value="${warehouse.address}"/>
                    </td>

                    <td>
                        <c:out value="${warehouse.managerName}"/>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${warehouse.active}">
                                Active
                            </c:when>
                            <c:otherwise>
                                Inactive
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>
                        <a href="${pageContext.request.contextPath}/warehouses/edit/${warehouse.warehouseId}">
                            Edit
                        </a>

                        <form action="${pageContext.request.contextPath}/warehouses/delete/${warehouse.warehouseId}"
                              method="POST"
                              style="display:inline;"
                              onsubmit="return confirm('Are you sure you want to delete this warehouse?');">

                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty warehouses}">
                <tr>
                    <td colspan="6">No warehouses found.</td>
                </tr>
            </c:if>
        </tbody>

    </table>

</main>

<%@ include file="../fragments/footer.jsp" %>
