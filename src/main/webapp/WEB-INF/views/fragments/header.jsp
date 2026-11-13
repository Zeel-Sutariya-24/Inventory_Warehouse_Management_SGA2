<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory & Warehouse Management</title>
</head>

<body>

    <header>
        <h2>Inventory Management System</h2>
        <nav>
            <a href="/">Dashboard</a> |
            <a href="/products">Products</a> |
            <a href="/suppliers">Suppliers</a> |
            <a href="/warehouses">Warehouses</a> |
            <a href="${pageContext.request.contextPath}/storage-locations">
                Storage Locations
            </a> |
            <a href="${pageContext.request.contextPath}/stock-lookup">
                Stock Lookup
            </a> |
            <a href="${pageContext.request.contextPath}/reorder-alerts">
                Reorder Alerts
            </a> |
            <a href="${pageContext.request.contextPath}/stock-valuation">
                Stock Valuation
            </a>
        </nav>
        <hr>
    </header>