<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Users</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Users"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Users</h2>
          <p>Manage application accounts and roles.</p>
        </div>
        <div class="admin-card__actions">
          <a class="btn btn-primary" href="/admin/users/new">New User</a>
        </div>
      </div>
      <c:choose>
        <c:when test="${empty users}">
          <div class="empty-state">
            No users available.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Username</th><th>Email</th><th>Display Name</th><th>Role</th><th>Enabled</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${users}" var="u">
                <tr>
                  <td>${u.id}</td>
                  <td>${u.username}</td>
                  <td>${u.email}</td>
                  <td>${u.displayName}</td>
                  <td>${u.role}</td>
                  <td>${u.enabled}</td>
                  <td>
                    <div class="actions-inline">
                      <a href="/admin/users/${u.id}/edit">Edit</a>
                      <form class="inline-form" action="/admin/users/${u.id}/delete" method="post" onsubmit="return confirm('Delete this user?');">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <button type="submit">Delete</button>
                      </form>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </div>
  </main>
</body>
</html>
