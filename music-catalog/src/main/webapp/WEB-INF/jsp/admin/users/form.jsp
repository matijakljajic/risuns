<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<c:set var="formAction" value="/admin/users" />
<c:if test="${not empty user.id}">
  <c:set var="formAction" value="${formAction}/${user.id}" />
</c:if>
<h2><c:if test="${empty user.id}">New</c:if><c:if test="${not empty user.id}">Edit</c:if> User</h2>
<form method="post" action="${formAction}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <p>
    <label>Username</label><br>
    <input type="text" name="username" value="<c:out value='${user.username}'/>" required maxlength="80">
  </p>
  <p>
    <label>Email</label><br>
    <input type="email" name="email" value="<c:out value='${user.email}'/>" required maxlength="255">
  </p>
  <p>
    <label>Display name</label><br>
    <input type="text" name="displayName" value="<c:out value='${user.displayName}'/>" maxlength="255">
  </p>
  <p>
    <label>Role</label><br>
    <select name="role" required>
      <c:forEach items="${roles}" var="r">
        <option value="${r}" <c:if test='${user.role == r}'>selected</c:if>>${r}</option>
      </c:forEach>
    </select>
  </p>
  <p>
    <label>Enabled</label>
    <input type="checkbox" name="enabled" <c:if test="${user.enabled}">checked</c:if> >
  </p>
  <p>
    <label>Password <small>(leave blank to keep current)</small></label><br>
    <input type="password" name="rawPassword" autocomplete="new-password">
  </p>
  <p><button type="submit">Save</button> <a href="/admin/users">Cancel</a></p>
</form>

<c:if test="${not empty org.springframework.validation.BindingResult.user}">
  <div style="color:red">
    <c:forEach items="${org.springframework.validation.BindingResult.user.allErrors}" var="e">
      <div>${e.defaultMessage}</div>
    </c:forEach>
  </div>
</c:if>
</body></html>
