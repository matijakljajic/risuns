<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Sign in - Music Catalog</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .login-shell { max-width: 520px; margin: 48px auto 12px; }
    .login-card { padding: 32px; }
    .login-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
      align-items: center;
    }
    .login-form .field { width: 100%; max-width: 340px; }
    label { font-weight: 600; display: block; margin-bottom: 6px; }
    input {
      width: 100%;
      max-width: 340px;
      padding: 10px 12px;
      border: 1px solid #d7dce7;
      border-radius: 0;
      font-size: 1rem;
    }
    .actions {
      display: flex;
      justify-content: flex-end;
    }
  </style>
</head>
<body>
  <div class="page-shell login-shell">
    <div class="content-card login-card">
      <h1>Welcome back</h1>

      <c:if test="${signupSuccess}">
        <div class="flash" style="border-color:#10b981;background:#ecfdf5;color:#065f46;">
          <c:choose>
            <c:when test="${not empty registeredUsername}">
              Account for <strong>${registeredUsername}</strong> is ready. Sign in to start exploring.
            </c:when>
            <c:otherwise>
              Account created successfully. Sign in to start exploring.
            </c:otherwise>
          </c:choose>
        </div>
      </c:if>

      <c:if test="${loginError}">
        <div class="flash" style="border-color:#b91c1c;background:#fef2f2;color:#7f1d1d;">
          Invalid username or password. Try again.
        </div>
      </c:if>

      <c:if test="${logoutMessage}">
        <div class="flash" style="border-color:#0f766e;background:#ecfdf5;color:#065f46;">
          You have been signed out.
        </div>
      </c:if>

      <form class="login-form" action="/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="field">
          <label for="username">Username</label>
          <input id="username" name="username" type="text" autocomplete="username" required/>
        </div>
        <div class="field">
          <label for="password">Password</label>
          <input id="password" name="password" type="password" autocomplete="current-password" required/>
        </div>
        <div class="actions">
          <button class="btn" type="submit">Sign in</button>
        </div>
      </form>
    </div>
  </div>

  <div class="page-shell login-shell" style="margin-top:0;">
    <div class="content-card" style="display:flex; flex-direction:column; gap:18px;">
      <div>
        <strong>Don't have an account?</strong>
        <div style="margin:6px 0 0;"></div>
        <a class="btn ghost" href="/signup">Sign up</a>
      </div>
      <div>
        <strong>Just want to look around?</strong>
        <div style="margin:6px 0 0;"></div>
        <a class="btn secondary" href="/">Continue without account</a>
      </div>
    </div>
  </div>
</body>
</html>
