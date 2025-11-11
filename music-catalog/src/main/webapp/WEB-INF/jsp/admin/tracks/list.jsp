<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Tracks</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Tracks"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Tracks</h2>
          <p>Manage track listings, ordering, and metadata.</p>
        </div>
        <div class="admin-card__actions">
          <a class="btn btn-primary" href="/admin/tracks/new">New Track</a>
        </div>
      </div>
      <c:choose>
        <c:when test="${empty tracks}">
          <div class="empty-state">
            No tracks recorded yet. <a href="/admin/tracks/new">Add your first track</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Title</th><th>#</th><th>Explicit</th><th>Album</th><th>Genres</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${tracks}" var="t">
                <tr>
                  <td>${t.id}</td>
                  <td>${t.title}</td>
                  <td>${t.trackNo}</td>
                  <td><c:out value="${t.explicit}"/></td>
                  <td><c:out value="${t.album != null ? t.album.title : ''}"/></td>
                  <td>
                    <c:forEach items="${t.genres}" var="g" varStatus="st">
                      ${g.name}<c:if test="${!st.last}">, </c:if>
                    </c:forEach>
                  </td>
                  <td>
                    <div class="actions-inline">
                      <a href="/admin/tracks/${t.id}/edit">Edit</a>
                      <form class="inline-form" action="/admin/tracks/${t.id}/delete" method="post" onsubmit="return confirm('Delete this track?');">
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
