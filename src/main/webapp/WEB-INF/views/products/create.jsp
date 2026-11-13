
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Add New Product</h1>

    <form:form
        action="${pageContext.request.contextPath}/products/save"
        method="POST"
        modelAttribute="productForm">

        <form:errors path="*" cssClass="error" />

        <p>
            <label>SKU</label><br>
            <form:input path="sku" />
            <form:errors path="sku" cssClass="error" />
        </p>

        <p>
            <label>Product Name</label><br>
            <form:input path="productName" />
            <form:errors path="productName" cssClass="error" />
        </p>

        <p>
            <label>Category</label><br>
            <form:select path="categoryId">
                <form:option value="" label="-- Select Category --" />
                <form:options items="${categories}"
                              itemValue="categoryId"
                              itemLabel="categoryName" />
            </form:select>
            <form:errors path="categoryId" cssClass="error" />
        </p>

        <p>
            <label>Unit of Measure</label><br>
            <form:input path="unitOfMeasure" placeholder="e.g. piece, box, kg" />
            <form:errors path="unitOfMeasure" cssClass="error" />
        </p>

        <p>
            <label>Unit Cost</label><br>
            <form:input path="unitCost" type="number" step="0.01" min="0" />
            <form:errors path="unitCost" cssClass="error" />
        </p>

        <p>
            <label>Reorder Level</label><br>
            <form:input path="reorderLevel" type="number" min="0" />
            <form:errors path="reorderLevel" cssClass="error" />
        </p>

        <p>
            <label>Barcode</label><br>
            <form:input path="barcode" />
            <form:errors path="barcode" cssClass="error" />
        </p>

        <p>
            <label>
                <form:checkbox path="active" />
                Active Product
            </label>
        </p>

        <button type="submit">Save Product</button>
        <a href="${pageContext.request.contextPath}/products">Cancel</a>

    </form:form>

</main>

<%@ include file="../fragments/footer.jsp" %>
