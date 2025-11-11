<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Albums</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Albums"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Albums</h2>
          <p>Manage album metadata, release years, and artists.</p>
        </div>
        <div class="admin-card__actions">
          <a class="btn btn-primary" href="/admin/albums/new">New Album</a>
        </div>
      </div>
      <c:choose>
        <c:when test="${empty albums}">
          <div class="empty-state">
            No albums recorded yet. <a href="/admin/albums/new">Create one now</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Title</th><th>Year</th><th>Artist</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${albums}" var="al">
                <tr>
                  <td>${al.id}</td>
                  <td>${al.title}</td>
                  <td>${al.releaseYear}</td>
                  <td><c:out value="${al.primaryArtist != null ? al.primaryArtist.name : ''}"/></td>
                  <td>
                    <div class="actions-inline">
                      <a href="/admin/albums/${al.id}/edit">Edit</a>
                      <form class="inline-form" action="/admin/albums/${al.id}/delete" method="post" onsubmit="return confirm('Delete this album?');">
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
