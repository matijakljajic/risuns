<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"/><title>Server Error</title></head>
<body>
  <h1>500 — Server Error</h1>
  <p>Something went wrong while processing your request.</p>
  <p><a href="/">← Back to Home</a></p>
  <!-- Show stack trace only when requested: /whatever?trace=true -->
  <c:if test="${param.trace == 'true' && not empty trace}">
    <h3>Trace</h3>
    <pre>${trace}</pre>
  </c:if>
</body>
</html>

