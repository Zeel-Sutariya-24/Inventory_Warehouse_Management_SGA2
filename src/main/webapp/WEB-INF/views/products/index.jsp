<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ include file="../fragments/header.jsp" %>

            <main class="container">

                <h1>Products</h1>

                <c:if test="${not empty successMessage}">
                    <p style="color:green;">
                        <c:out value="${successMessage}" />
                    </p>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <p style="color:red;">
                        <c:out value="${errorMessage}" />
                    </p>
                </c:if>

                <p>
                    <a href="${pageContext.request.contextPath}/products/new">
                        + Add New Product
                    </a>
                </p>
                <p>View all products registered in the inventory system.</p>

                <table border="1" cellpadding="10" cellspacing="0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>SKU</th>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Unit</th>
                            <th>Unit Cost</th>
                            <th>Reorder Level</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>${product.productId}</td>
                                <td>
                                    <c:out value="${product.sku}" />
                                </td>
                                <td>
                                    <c:out value="${product.productName}" />
                                </td>
                                <td>
                                    <c:out value="${product.category.categoryName}" />
                                </td>
                                <td>
                                    <c:out value="${product.unitOfMeasure}" />
                                </td>
                                <td>${product.unitCost}</td>
                                <td>${product.reorderLevel}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.active}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <a href="${pageContext.request.contextPath}/products/edit/${product.productId}">
                                        Edit
                                    </a>

                                    <form
                                        action="${pageContext.request.contextPath}/products/delete/${product.productId}"
                                        method="POST" style="display:inline;"
                                        onsubmit="return confirm('Are you sure you want to delete this product?');">

                                        <button type="submit">Delete</button>
                                    </form>
                                </td>

                            </tr>

                        </c:forEach>

                        <c:if test="${empty products}">
                            <tr>
                                <td colspan="8">No products found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

            </main>

            <%@ include file="../fragments/footer.jsp" %>