<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · User</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Users"/>
  <c:set var="formAction" value="/admin/users"/>
  <c:set var="isNew" value="${empty user.id}"/>
  <c:if test="${not empty user.id}">
    <c:set var="formAction" value="${formAction}/${user.id}"/>
  </c:if>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2><c:choose><c:when test="${isNew}">Create user</c:when><c:otherwise>Edit user</c:otherwise></c:choose></h2>
          <p>${isNew ? "Invite a new user." : "Update account details."}</p>
        </div>
      </div>
      <form class="admin-form" method="post" action="${formAction}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="form-row">
          <label for="username">Username</label>
          <input id="username" type="text" name="username" value="<c:out value='${user.username}'/>" required maxlength="80">
        </div>
        <div class="form-row">
          <label for="email">Email</label>
          <input id="email" type="email" name="email" value="<c:out value='${user.email}'/>" required maxlength="255">
        </div>
        <div class="form-row">
          <label for="displayName">Display name</label>
          <input id="displayName" type="text" name="displayName" value="<c:out value='${user.displayName}'/>" maxlength="255">
        </div>
        <div class="form-row">
          <label for="role">Role</label>
          <select id="role" name="role" required>
            <c:forEach items="${roles}" var="r">
              <option value="${r}" <c:if test='${user.role == r}'>selected</c:if>>${r}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-row">
          <label>
            <input type="hidden" name="_enabled" value="on">
            <input type="checkbox" name="enabled" value="true" <c:if test="${user.enabled}">checked</c:if>> Enabled
          </label>
        </div>
        <div class="form-row">
          <label for="password">Password <span class="helper-text">(leave blank to keep current)</span></label>
          <input id="password" type="password" name="rawPassword" autocomplete="new-password">
        </div>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit">
            <c:choose><c:when test="${isNew}">Create</c:when><c:otherwise>Save changes</c:otherwise></c:choose>
          </button>
          <a class="btn btn-secondary" href="/admin/users">Cancel</a>
        </div>
      </form>

      <c:if test="${not empty org.springframework.validation.BindingResult.user}">
        <div class="form-errors">
          <c:forEach items="${org.springframework.validation.BindingResult.user.allErrors}" var="e">
            <div>${e.defaultMessage}</div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </main>
</body>
</html>
