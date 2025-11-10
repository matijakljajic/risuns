<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Error ${status} – ${error}</title>
  <style>
    body { font-family: system-ui, sans-serif; margin: 2rem; }
    .card { max-width: 720px; border: 1px solid #ddd; border-radius: 12px; padding: 1.25rem; }
    .muted { color: #666; }
    pre { overflow: auto; background: #f8f8f8; padding: 1rem; border-radius: 8px; }
    a { text-decoration: none; }
  </style>
</head>
<body>
  <div class="card">
    <h1>Oops — ${status} ${error}</h1>
    <p class="muted">${message}</p>

    <ul class="muted">
      <li><b>Path:</b> ${path}</li>
      <li><b>Timestamp:</b> ${timestamp}</li>
    </ul>

    <!-- Show stack trace only when requested: /whatever?trace=true -->
    <c:if test="${param.trace == 'true' && not empty trace}">
      <h3>Trace</h3>
      <pre>${trace}</pre>
    </c:if>

    <p><a href="/">← Back to Home</a></p>
  </div>
</body>
</html>

