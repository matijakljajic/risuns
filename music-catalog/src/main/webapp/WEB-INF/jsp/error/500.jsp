<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>500 — Server Error</title>
  <style>
    body {
      font-family: "Segoe UI", Arial, sans-serif;
      margin: 0;
      padding: 24px;
      background-color: #f7f7f7;
      color: #111;
    }

    .error-wrapper {
      max-width: 900px;
      background: #ffffff;
      border: 1px solid #eee;
      padding: 16px 20px 18px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.06);
    }

    h1 {
      margin: 0 0 8px 0;
      font-size: 1.4rem;
      font-weight: 600;
    }

    p {
      margin: 4px 0 8px 0;
      font-size: 0.95rem;
    }

    .muted {
      color: #666;
      font-size: 0.9rem;
    }

    pre {
      margin-top: 8px;
      padding: 10px 12px;
      background: #f8f8f8;
      border: 1px solid #eee;
      font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
      font-size: 0.8rem;
      overflow: auto;
      border-radius: 0;
    }

    a {
      color: #111;
      text-decoration: none;
      font-weight: 500;
    }

    a:hover {
      text-decoration: underline;
    }

    .back-link {
      display: inline-block;
      margin-top: 14px;
      font-size: 0.9rem;
    }
  </style>
</head>
<body>
  <div class="error-wrapper">
    <h1>500 — Server Error</h1>
    <p>Something went wrong while processing your request.</p>
    <p class="muted">If this keeps happening, contact the administrator or check the server logs.</p>

    <!-- Show stack trace only when requested: /...?trace=true -->
    <c:if test="${param.trace == 'true' && not empty trace}">
      <h3>Trace</h3>
      <pre>${trace}</pre>
    </c:if>

    <p class="back-link"><a href="/">← Back to Home</a></p>
  </div>
</body>
</html>
