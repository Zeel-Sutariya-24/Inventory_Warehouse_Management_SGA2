
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Edit Warehouse</h1>

    <form:form
        action="${pageContext.request.contextPath}/warehouses/update/${warehouseId}"
        method="POST"
        modelAttribute="warehouseForm">

        <form:errors path="*" cssClass="error"/>

        <p>
            <label>Warehouse Name</label><br>
            <form:input path="warehouseName"/>
            <form:errors path="warehouseName" cssClass="error"/>
        </p>

        <p>
            <label>Address</label><br>
            <form:textarea path="address" rows="3"/>
            <form:errors path="address" cssClass="error"/>
        </p>

        <p>
            <label>Manager Name</label><br>
            <form:input path="managerName"/>
            <form:errors path="managerName" cssClass="error"/>
        </p>

        <p>
            <label>
                <form:checkbox path="active"/>
                Active Warehouse
            </label>
        </p>

        <button type="submit">Update Warehouse</button>
        <a href="${pageContext.request.contextPath}/warehouses">Cancel</a>

    </form:form>

</main>

<%@ include file="../fragments/footer.jsp" %>
