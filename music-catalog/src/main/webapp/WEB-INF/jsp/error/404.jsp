<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>404 — Not Found</title>
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

    code {
      font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
      font-size: 0.9rem;
      background: #f5f5f5;
      padding: 2px 4px;
      border: 1px solid #eee;
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
    <h1>404 — Not Found</h1>
    <p>The page <code>${path}</code> doesn’t exist.</p>

    <!-- Show stack trace only when requested: /...?trace=true -->
    <c:if test="${param.trace == 'true' && not empty trace}">
      <h3>Trace</h3>
      <pre>${trace}</pre>
    </c:if>

    <p class="back-link"><a href="/">← Back to Home</a></p>
  </div>
</body>
</html>
