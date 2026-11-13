
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../fragments/header.jsp" %>

<main class="container">

    <h1>${errorTitle}</h1>

    <div style="padding: 20px; border: 1px solid #ddd; border-radius: 8px; margin: 20px 0;">
        <h2>Sorry!</h2>
        <p>${errorMessage}</p>
        <p>The requested page or record may not exist or may have been removed.</p>
    </div>

    <a href="${pageContext.request.contextPath}/">
        Return to Dashboard
    </a>

</main>

<%@ include file="../fragments/footer.jsp" %>
