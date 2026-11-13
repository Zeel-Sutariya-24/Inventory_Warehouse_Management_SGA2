
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>Edit Supplier</h1>

    <form:form
        action="${pageContext.request.contextPath}/suppliers/update/${supplierId}"
        method="POST"
        modelAttribute="supplierForm">

        <form:errors path="*" cssClass="error"/>

        <p>
            <label>Supplier Name</label><br>
            <form:input path="supplierName"/>
            <form:errors path="supplierName" cssClass="error"/>
        </p>

        <p>
            <label>Contact Person</label><br>
            <form:input path="contactName"/>
            <form:errors path="contactName" cssClass="error"/>
        </p>

        <p>
            <label>Phone</label><br>
            <form:input path="phone"/>
            <form:errors path="phone" cssClass="error"/>
        </p>

        <p>
            <label>Email</label><br>
            <form:input path="email" type="email"/>
            <form:errors path="email" cssClass="error"/>
        </p>

        <p>
            <label>Address</label><br>
            <form:textarea path="address" rows="3"/>
            <form:errors path="address" cssClass="error"/>
        </p>

        <p>
            <label>
                <form:checkbox path="active"/>
                Active Supplier
            </label>
        </p>

        <button type="submit">Update Supplier</button>
        <a href="${pageContext.request.contextPath}/suppliers">Cancel</a>

    </form:form>

</main>

<%@ include file="../fragments/footer.jsp" %>
