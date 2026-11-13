
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Inventory & Warehouse Management</h1>
    <p>Welcome to your inventory management dashboard.</p>

    <section>
        <h2>System Overview</h2>
        <p>Manage products, suppliers, warehouses, purchase orders, stock transfers and dispatches.</p>
    </section>

    <hr>

    <section>
        <h2>Warehouse Inventory Summary</h2>

        <label for="warehouseSelect">Select Warehouse:</label>

        <select id="warehouseSelect">
            <option value="">-- Select Warehouse --</option>

            <c:forEach var="warehouse" items="${warehouses}">
                <option value="${warehouse.warehouseId}">
                    <c:out value="${warehouse.warehouseName}"/>
                </option>
            </c:forEach>
        </select>

        <p id="summaryMessage">Please select a warehouse to view its inventory summary.</p>

        <div id="summaryCards" style="display:none; margin-top:20px;">

            <div style="border:1px solid #ccc; padding:20px; margin-bottom:12px;">
                <h3>Total Products</h3>
                <p id="totalProducts">0</p>
            </div>

            <div style="border:1px solid #ccc; padding:20px; margin-bottom:12px;">
                <h3>Total Units Available</h3>
                <p id="totalUnits">0</p>
            </div>

            <div style="border:1px solid #ccc; padding:20px; margin-bottom:12px;">
                <h3>Low Stock Products</h3>
                <p id="lowStockProducts">0</p>
            </div>

        </div>
    </section>

</main>

<script>
document.addEventListener("DOMContentLoaded", function () {

    const warehouseSelect = document.getElementById("warehouseSelect");
    const summaryCards = document.getElementById("summaryCards");
    const summaryMessage = document.getElementById("summaryMessage");

    warehouseSelect.addEventListener("change", function () {

        const warehouseId = this.value;

        if (!warehouseId) {
            summaryCards.style.display = "none";
            summaryMessage.textContent =
                "Please select a warehouse to view its inventory summary.";
            return;
        }

        summaryMessage.textContent = "Loading inventory summary...";
        summaryCards.style.display = "none";

        fetch("/api/stock-lookup/" + encodeURIComponent(warehouseId) + "/summary")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Unable to load inventory summary.");
                }
                return response.json();
            })
            .then(data => {

                document.getElementById("totalProducts").textContent =
                    data.totalProducts ?? 0;

                document.getElementById("totalUnits").textContent =
                    data.totalUnits ?? 0;

                document.getElementById("lowStockProducts").textContent =
                    data.lowStockProducts ?? 0;

                summaryCards.style.display = "block";
                summaryMessage.textContent =
                    "Inventory summary loaded successfully.";

            })
            .catch(error => {
                summaryCards.style.display = "none";
                summaryMessage.textContent = error.message;
            });

    });

});
</script>

<%@ include file="../fragments/footer.jsp" %>
