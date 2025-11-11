<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Error ${status} – ${error}</title>
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

    .muted {
      color: #666;
      font-size: 0.9rem;
    }

    .meta-list {
      margin: 10px 0 14px 0;
      padding: 0;
      list-style: none;
      font-size: 0.9rem;
      color: #444;
    }

    .meta-list li {
      margin-bottom: 2px;
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
    <h1>Oops — ${status} ${error}</h1>
    <p class="muted">${message}</p>

    <ul class="meta-list">
      <li><strong>Path:</strong> ${path}</li>
      <li><strong>Timestamp:</strong> ${timestamp}</li>
    </ul>

    <!-- Show stack trace only when requested: /... ?trace=true -->
    <c:if test="${param.trace == 'true' && not empty trace}">
      <h3>Trace</h3>
      <pre>${trace}</pre>
    </c:if>

    <p class="back-link"><a href="/">← Back to Home</a></p>
  </div>
</body>
</html>
