<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Albums</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .album-cover {
      width: 64px;
      height: 64px;
      object-fit: cover;
      border: 1px solid #e2e8f0;
      background: #f1f5f9;
    }
    .album-title {
      font-weight: 600;
      margin-bottom: 4px;
    }
    .album-meta {
      font-size: 0.85rem;
      color: #64748b;
    }
  </style>
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
      <form class="admin-filter" method="get">
        <div class="filter-field">
          <label for="album-q">Search</label>
          <input id="album-q" type="text" name="q" value="${query}" placeholder="Search by title"/>
        </div>
        <div class="filter-field">
          <label for="album-year">Year</label>
          <input id="album-year" type="number" name="year" value="${yearFilter}" min="1900" max="2100" placeholder="YYYY"/>
        </div>
        <button class="btn btn-secondary" type="submit">Apply</button>
        <c:if test="${not empty query or yearFilter ne null}">
          <a class="btn-link" href="/admin/albums">Reset</a>
        </c:if>
      </form>
      <c:choose>
        <c:when test="${empty albums}">
          <div class="empty-state">
            No albums recorded yet. <a href="/admin/albums/new">Create one now</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Album</th><th>Cover</th><th>Release</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${albums}" var="al">
                <tr>
                  <td>${al.id}</td>
                  <td>
                    <div class="album-title">${al.title}</div>
                    <div class="album-meta">
                      <c:choose>
                        <c:when test="${al.primaryArtist != null}">
                          ${al.primaryArtist.name}
                        </c:when>
                        <c:otherwise>No primary artist</c:otherwise>
                      </c:choose>
                    </div>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty al.coverUrl}">
                        <img src="${al.coverUrl}" alt="${al.title}" class="album-cover"/>
                      </c:when>
                      <c:otherwise>
                        <span class="album-meta">No cover</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <div class="album-meta">
                      <strong>Year:</strong>
                      <c:choose>
                        <c:when test="${al.releaseYear != null}">
                          ${al.releaseYear}
                        </c:when>
                        <c:otherwise>n/a</c:otherwise>
                      </c:choose>
                    </div>
                  </td>
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
