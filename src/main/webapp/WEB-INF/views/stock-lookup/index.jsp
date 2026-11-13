
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<h2>Stock Lookup by Warehouse</h2>

<p>Select a warehouse to view its current inventory.</p>

<div style="margin-bottom:20px;">
    <label for="warehouseSelect">Select Warehouse:</label>

    <select id="warehouseSelect">
        <option value="">-- Select Warehouse --</option>

        <c:forEach var="warehouse" items="${warehouses}">
            <option value="${warehouse.warehouseId}">
                ${warehouse.warehouseName}
            </option>
        </c:forEach>
    </select>
</div>

<p id="loadingMessage" style="display:none;">
    Loading stock information...
</p>

<p id="errorMessage" style="color:red;"></p>

<table border="1" cellpadding="10" cellspacing="0" width="100%"
       id="stockTable" style="display:none;">

    <thead>
        <tr>
            <th>SKU</th>
            <th>Product Name</th>
            <th>Quantity Available</th>
            <th>Reorder Level</th>
            <th>Stock Status</th>
        </tr>
    </thead>

    <tbody id="stockTableBody">
    </tbody>
</table>

<p id="emptyMessage" style="display:none;">
    No stock records found for this warehouse.
</p>

<script>
    const warehouseSelect = document.getElementById("warehouseSelect");
    const stockTable = document.getElementById("stockTable");
    const stockTableBody = document.getElementById("stockTableBody");
    const loadingMessage = document.getElementById("loadingMessage");
    const errorMessage = document.getElementById("errorMessage");
    const emptyMessage = document.getElementById("emptyMessage");

    const contextPath = "${pageContext.request.contextPath}";

    warehouseSelect.addEventListener("change", function () {

        const warehouseId = this.value;

        stockTable.style.display = "none";
        emptyMessage.style.display = "none";
        errorMessage.textContent = "";
        stockTableBody.innerHTML = "";

        if (!warehouseId) {
            return;
        }

        loadingMessage.style.display = "block";

        fetch(contextPath + "/api/stock-lookup/" + warehouseId)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Could not load stock information.");
                }
                return response.json();
            })
            .then(stockRecords => {

                loadingMessage.style.display = "none";

                if (stockRecords.length === 0) {
                    emptyMessage.style.display = "block";
                    return;
                }

                stockRecords.forEach(stock => {

                    const row = document.createElement("tr");

                    const values = [
                        stock.sku,
                        stock.productName,
                        stock.quantityOnHand,
                        stock.reorderLevel,
                        stock.status
                    ];

                    values.forEach(value => {
                        const cell = document.createElement("td");
                        cell.textContent = value;
                        row.appendChild(cell);
                    });

                    stockTableBody.appendChild(row);
                });

                stockTable.style.display = "table";
            })
            .catch(error => {
                loadingMessage.style.display = "none";
                errorMessage.textContent = error.message;
            });
    });
</script>

<%@ include file="../fragments/footer.jsp" %>
