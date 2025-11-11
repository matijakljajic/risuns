<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<h2>Users</h2>
<p><a href="/admin">Admin home</a> | <a href="/admin/users/new">New User</a></p>

<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Username</th><th>Email</th><th>Display Name</th><th>Role</th><th>Enabled</th><th>Actions</th></tr>
  <c:forEach items="${users}" var="u">
    <tr>
      <td>${u.id}</td>
      <td>${u.username}</td>
      <td>${u.email}</td>
      <td>${u.displayName}</td>
      <td>${u.role}</td>
      <td>${u.enabled}</td>
      <td>
        <a href="/admin/users/${u.id}/edit">Edit</a>
        <form action="/admin/users/${u.id}/delete" method="post" style="display:inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" onclick="return confirm('Delete?')">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body></html>
