<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Artists</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Artists"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Artists</h2>
          <p>Manage artist information and artwork.</p>
        </div>
        <div class="admin-card__actions">
          <a class="btn btn-primary" href="/admin/artists/new">New Artist</a>
        </div>
      </div>
      <c:choose>
        <c:when test="${empty artists}">
          <div class="empty-state">
            No artists yet. <a href="/admin/artists/new">Add the first artist</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Name</th><th>Image URL</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${artists}" var="a">
                <tr>
                  <td>${a.id}</td>
                  <td>${a.name}</td>
                  <td><c:out value="${a.imageUrl}"/></td>
                  <td>
                    <div class="actions-inline">
                      <a href="/admin/artists/${a.id}/edit">Edit</a>
                      <form class="inline-form" action="/admin/artists/${a.id}/delete" method="post" onsubmit="return confirm('Delete this artist?');">
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
