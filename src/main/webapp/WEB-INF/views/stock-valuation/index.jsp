
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<h2>Stock Valuation</h2>

<p>
    Inventory valuation is calculated using quantity on hand
    multiplied by the product's unit cost.
</p>

<h3>
    Total Inventory Value:
    <strong>Rs <c:out value="${totalValuation}"/></strong>
</h3>

<h3>Warehouse-wise Valuation</h3>

<table border="1" cellpadding="10" cellspacing="0" width="60%">
    <thead>
        <tr>
            <th>Warehouse</th>
            <th>Total Stock Value (Rs)</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="entry" items="${warehouseTotals}">
            <tr>
                <td><c:out value="${entry.key}"/></td>
                <td><c:out value="${entry.value}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<h3>Product-wise Stock Valuation</h3>

<table border="1" cellpadding="10" cellspacing="0" width="100%">
    <thead>
        <tr>
            <th>Warehouse</th>
            <th>SKU</th>
            <th>Product</th>
            <th>Quantity</th>
            <th>Unit Cost (Rs)</th>
            <th>Total Value (Rs)</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="record" items="${records}">
            <tr>
                <td><c:out value="${record.warehouseName}"/></td>
                <td><c:out value="${record.sku}"/></td>
                <td><c:out value="${record.productName}"/></td>
                <td><c:out value="${record.quantityOnHand}"/></td>
                <td><c:out value="${record.unitCost}"/></td>
                <td><c:out value="${record.totalValue}"/></td>
            </tr>
        </c:forEach>

        <c:if test="${empty records}">
            <tr>
                <td colspan="6">No stock available for valuation.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../fragments/footer.jsp" %>
