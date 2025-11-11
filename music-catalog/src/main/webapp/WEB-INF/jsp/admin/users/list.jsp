<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Users</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .user-name { font-weight: 600; font-size: 1rem; }
    .user-meta { font-size: 0.85rem; color: #64748b; }
    .status-pill {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      padding: 4px 10px;
      border-radius: 999px;
      font-size: 0.8rem;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .status-pill.enabled {
      background: #dcfce7;
      border: 1px solid #86efac;
      color: #166534;
    }
    .status-pill.disabled {
      background: #fee2e2;
      border: 1px solid #fecaca;
      color: #991b1b;
    }
  </style>
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
      <form class="admin-filter" method="get">
        <div class="filter-field">
          <label for="user-q">Search</label>
          <input id="user-q" type="text" name="q" value="${query}" placeholder="Search by name"/>
        </div>
        <div class="filter-field">
          <label for="user-role">Role</label>
          <select id="user-role" name="role">
            <option value="">All roles</option>
            <c:forEach items="${roles}" var="role">
              <option value="${role.name()}" <c:if test="${roleFilter == role.name()}">selected</c:if>>
                ${role.name()}
              </option>
            </c:forEach>
          </select>
        </div>
        <div class="filter-field">
          <label for="user-status">Status</label>
          <select id="user-status" name="status">
            <option value="">Any</option>
            <option value="enabled" <c:if test="${statusFilter == 'enabled'}">selected</c:if>>Enabled</option>
            <option value="disabled" <c:if test="${statusFilter == 'disabled'}">selected</c:if>>Disabled</option>
          </select>
        </div>
        <button class="btn btn-secondary" type="submit">Apply</button>
        <c:if test="${not empty query or not empty roleFilter or not empty statusFilter}">
          <a class="btn-link" href="/admin/users">Reset</a>
        </c:if>
      </form>
      <c:choose>
        <c:when test="${empty users}">
          <div class="empty-state">
            No users available.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>User</th><th>Contact</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${users}" var="u">
                <tr>
                  <td>${u.id}</td>
                  <td>
                    <div class="user-name">${u.username}</div>
                    <c:if test="${not empty u.displayName}">
                      <div class="user-meta">${u.displayName}</div>
                    </c:if>
                  </td>
                  <td>
                    <div class="user-meta">${u.email}</div>
                    <div class="user-meta">
                      <c:choose>
                        <c:when test="${u.notifyOnMessage}">Notifications enabled</c:when>
                        <c:otherwise>No notifications</c:otherwise>
                      </c:choose>
                    </div>
                  </td>
                  <td>
                    <div style="display:flex;flex-direction:column;gap:6px;">
                      <span class="admin-chip">${u.role}</span>
                      <span class="status-pill ${u.enabled ? 'enabled' : 'disabled'}">
                        <c:choose>
                          <c:when test="${u.enabled}">Enabled</c:when>
                          <c:otherwise>Disabled</c:otherwise>
                        </c:choose>
                      </span>
                    </div>
                  </td>
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
