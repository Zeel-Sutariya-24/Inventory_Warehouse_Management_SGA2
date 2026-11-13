
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<h2>Reorder Alerts</h2>

<p>
    Products are displayed here when their available quantity
    is less than or equal to their reorder level.
</p>

<h3>Total Reorder Alerts: ${alerts.size()}</h3>

<table border="1" cellpadding="10" cellspacing="0" width="100%">
    <thead>
        <tr>
            <th>Warehouse</th>
            <th>SKU</th>
            <th>Product</th>
            <th>Quantity Available</th>
            <th>Reorder Level</th>
            <th>Status</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="alert" items="${alerts}">
            <tr>
                <td>${alert.warehouse.warehouseName}</td>
                <td>${alert.product.sku}</td>
                <td>${alert.product.productName}</td>
                <td>${alert.quantityOnHand}</td>
                <td>${alert.product.reorderLevel}</td>
                <td>
                    <c:choose>
                        <c:when test="${alert.quantityOnHand == 0}">
                            <strong style="color:red;">OUT OF STOCK</strong>
                        </c:when>
                        <c:otherwise>
                            <strong style="color:#d97706;">LOW STOCK</strong>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty alerts}">
            <tr>
                <td colspan="6">
                    No reorder alerts. All warehouse stock levels are sufficient.
                </td>
            </tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../fragments/footer.jsp" %>
