<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Sign up - Music Catalog</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .auth-signup-page .signup-shell { max-width: 720px; margin: 48px auto; }
    .auth-signup-page .signup-card { padding: 32px 40px; }
    .auth-signup-page .signup-form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }
    .auth-signup-page .field { display: flex; flex-direction: column; gap: 6px; }
    .auth-signup-page label { font-weight: 600; }
    .auth-signup-page input[type="text"],
    .auth-signup-page input[type="email"],
    .auth-signup-page input[type="password"] {
      width: 100%;
      padding: 10px 12px;
      border: 1px solid #d7dce7;
      font-size: 1rem;
    }
    .auth-signup-page .field-error {
      color: #b91c1c;
      font-size: 0.85rem;
    }
    .auth-signup-page .split {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 18px;
    }
    .auth-signup-page .support-panel {
      margin-top: 28px;
      display: grid;
      gap: 16px;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
    }
    .auth-signup-page .support-card {
      border: 1px solid #e4e7ef;
      background: #f8fafc;
      padding: 18px 20px;
      display: flex;
      flex-direction: column;
      gap: 10px;
    }
    .auth-signup-page .support-card p { margin: 0; color: #4b5563; }
    .auth-signup-page .support-card a { text-decoration: none; }
    .auth-signup-page .checkbox-row {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 0.95rem;
      font-weight: 500;
    }
  </style>
</head>
<body class="auth-signup-page">
  <div class="page-shell signup-shell">
    <div class="content-card signup-card">
      <h1>Create your account</h1>
      <p style="margin-top:-6px;color:#6b7280;">Save playlists, message friends, and keep your listening stats in one place.</p>

      <form:form cssClass="signup-form" method="post" action="/signup" modelAttribute="signupForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="split">
          <div class="field">
            <label for="username">Username</label>
            <form:input path="username" id="username" autocomplete="username"/>
            <form:errors path="username" cssClass="field-error"/>
          </div>
          <div class="field">
            <label for="displayName">Display name (optional)</label>
            <form:input path="displayName" id="displayName" autocomplete="name"/>
            <form:errors path="displayName" cssClass="field-error"/>
          </div>
        </div>

        <div class="field">
          <label for="email">Email</label>
          <form:input path="email" id="email" type="email" autocomplete="email"/>
          <form:errors path="email" cssClass="field-error"/>
        </div>

        <div class="split">
          <div class="field">
            <label for="password">Password</label>
            <form:password path="password" id="password" autocomplete="new-password"/>
            <form:errors path="password" cssClass="field-error"/>
          </div>
          <div class="field">
            <label for="confirmPassword">Confirm password</label>
            <form:password path="confirmPassword" id="confirmPassword" autocomplete="new-password"/>
            <form:errors path="confirmPassword" cssClass="field-error"/>
          </div>
        </div>

        <label class="checkbox-row">
          <form:checkbox path="notifyOnMessage"/>
          Email me when someone sends me a message.
        </label>

        <div class="actions" style="justify-content:flex-start;">
          <button class="btn" type="submit">Create account</button>
        </div>
      </form:form>

      <div class="support-panel">
        <div class="support-card">
          <strong>Already on Music Catalog?</strong>
          <p>Head back to sign in and pick up where you left off.</p>
          <a class="btn ghost" href="/login">Go to login</a>
        </div>
        <div class="support-card">
          <strong>Just browsing?</strong>
          <p>All public artists, albums, and playlists remain available without an account.</p>
          <a class="btn secondary" href="/">Use without account</a>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
