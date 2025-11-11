<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Error</title>
  <style>
    body {
      font-family: "Segoe UI", Arial, sans-serif;
      margin: 0;
      padding: 32px;
      background-color: #f5f6fb;
      color: #111;
    }
    .error-panel {
      max-width: 760px;
      background: #ffffff;
      border: 1px solid #e1e5ef;
      padding: 20px 24px;
      box-shadow: 0 2px 8px rgba(15,23,42,0.08);
    }
    h1 {
      margin: 0 0 6px 0;
      font-size: 1.35rem;
      font-weight: 600;
    }
    .muted {
      color: #4b5563;
      font-size: 0.95rem;
      margin-bottom: 14px;
    }
    pre {
      margin: 12px 0 0;
      padding: 10px 12px;
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
      font-size: 0.85rem;
      overflow: auto;
    }
    .back-link {
      display: inline-block;
      margin-top: 18px;
      font-size: 0.9rem;
    }
    a {
      color: #111;
      font-weight: 500;
      text-decoration: none;
    }
    a:hover { text-decoration: underline; }
  </style>
</head>
<body>
  <div class="error-panel">
    <h1>Operation failed</h1>
    <p class="muted">
      <c:out value="${dbError != null ? dbError : 'An unexpected error occurred while processing your request.'}"/>
    </p>

    <c:if test="${not empty requestScope['jakarta.servlet.error.message']}">
      <pre><c:out value="${requestScope['jakarta.servlet.error.message']}"/></pre>
    </c:if>

    <p class="back-link"><a href="/admin">← Back to Admin dashboard</a></p>
  </div>
</body>
</html>
