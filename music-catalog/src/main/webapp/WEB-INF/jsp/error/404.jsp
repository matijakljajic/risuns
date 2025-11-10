<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"/><title>Not Found</title></head>
<body>
  <h1>404 — Not Found</h1>
  <p>The page <code>${path}</code> doesn’t exist.</p>
  <p><a href="/">← Back to Home</a></p>
  <!-- Show stack trace only when requested: /whatever?trace=true -->
  <c:if test="${param.trace == 'true' && not empty trace}">
    <h3>Trace</h3>
    <pre>${trace}</pre>
  </c:if>
</body>
</html>

