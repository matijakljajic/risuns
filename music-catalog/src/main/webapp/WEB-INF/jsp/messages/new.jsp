<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>New message</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .field-error {
      color: #b91c1c;
      font-size: 0.85rem;
    }
    textarea, select {
      width: 100%;
      border: 1px solid #cbd5f5;
      padding: 8px;
      font-family: inherit;
      box-sizing: border-box;
    }
    label {
      font-weight: 600;
      display: block;
      margin-top: 16px;
      margin-bottom: 6px;
    }
  </style>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/messages">← Back to messages</a>
    <div class="content-card">
      <h1>Compose message</h1>
      <form:form method="post" action="/messages" modelAttribute="messageForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <c:choose>
          <c:when test="${preselectedRecipient != null}">
            <label>To</label>
            <p>
              <a href="/users/${preselectedRecipient.id}">${preselectedRecipient.label}</a>
              (<a href="/messages/new">change</a>)
            </p>
            <form:hidden path="recipientId"/>
          </c:when>
          <c:otherwise>
            <label for="recipientId">Recipient</label>
            <form:select path="recipientId" id="recipientId">
              <form:option value="">-- Select user --</form:option>
              <form:options items="${recipientOptions}" itemValue="id" itemLabel="label"/>
            </form:select>
            <form:errors path="recipientId" cssClass="field-error"/>
          </c:otherwise>
        </c:choose>

        <label for="content">Message</label>
        <form:textarea path="content" id="content" rows="6"/>
        <form:errors path="content" cssClass="field-error"/>

        <c:set var="globalErrors" value="${org.springframework.validation.BindingResult.messageForm.globalErrors}"/>
        <c:if test="${not empty globalErrors}">
          <ul class="field-error">
            <c:forEach items="${globalErrors}" var="error">
              <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
          </ul>
        </c:if>

        <div style="margin-top:18px; display:flex; justify-content:flex-end;">
          <button type="submit" class="btn" style="width:auto;">Send</button>
        </div>
      </form:form>
    </div>
  </div>
</body>
</html>
