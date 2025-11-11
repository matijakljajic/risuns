<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Genres</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .genre-name {
      font-weight: 600;
      font-size: 1rem;
    }
    .genre-note {
      font-size: 0.85rem;
      color: #64748b;
    }
  </style>
</head>
<body>
  <c:set var="pageTitle" value="Genres"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Genres</h2>
          <p>Manage the catalog’s genre taxonomy.</p>
        </div>
        <div class="admin-card__actions">
          <a class="btn btn-primary" href="/admin/genres/new">New Genre</a>
        </div>
      </div>
      <form class="admin-filter" method="get">
        <div class="filter-field">
          <label for="genre-q">Search</label>
          <input id="genre-q" type="text" name="q" value="${query}" placeholder="Search by name"/>
        </div>
        <button class="btn btn-secondary" type="submit">Apply</button>
        <c:if test="${not empty query}">
          <a class="btn-link" href="/admin/genres">Reset</a>
        </c:if>
      </form>
      <c:choose>
        <c:when test="${empty genres}">
          <div class="empty-state">
            No genres yet. <a href="/admin/genres/new">Create the first genre</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Name</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${genres}" var="g">
                <tr>
                  <td>${g.id}</td>
                  <td>
                    <div class="genre-name">${g.name}</div>
                    <div class="genre-note">Internal ID #${g.id}</div>
                  </td>
                  <td>
                    <div class="actions-inline">
                      <a href="/admin/genres/${g.id}/edit">Edit</a>
                      <form class="inline-form" action="/admin/genres/${g.id}/delete" method="post" onsubmit="return confirm('Delete this genre?');">
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
