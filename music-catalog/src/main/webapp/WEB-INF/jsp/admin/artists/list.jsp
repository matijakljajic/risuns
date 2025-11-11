<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Artists</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .artist-img {
      width: 72px;
      height: 72px;
      object-fit: cover;
      border: 1px solid #e2e8f0;
      background: #f1f5f9;
    }
    .artist-name {
      font-weight: 600;
      font-size: 1rem;
      margin-bottom: 4px;
    }
    .artist-meta {
      font-size: 0.82rem;
      color: #64748b;
    }
    .artist-bio {
      max-width: 420px;
      font-size: 0.92rem;
      color: #334155;
      line-height: 1.4;
    }
    .artist-bio.muted {
      color: #94a3b8;
      font-style: italic;
    }
  </style>
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
      <form class="admin-filter" method="get">
        <div class="filter-field">
          <label for="artist-q">Search</label>
          <input id="artist-q" type="text" name="q" value="${query}" placeholder="Search by name"/>
        </div>
        <button class="btn btn-secondary" type="submit">Apply</button>
        <c:if test="${not empty query}">
          <a class="btn-link" href="/admin/artists">Reset</a>
        </c:if>
      </form>
      <c:choose>
        <c:when test="${empty artists}">
          <div class="empty-state">
            No artists yet. <a href="/admin/artists/new">Add the first artist</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Artist</th><th>Image</th><th>Bio preview</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${artists}" var="a">
                <tr>
                  <td>${a.id}</td>
                  <td>
                    <div class="artist-name">${a.name}</div>
                    <c:if test="${not empty a.imageUrl}">
                      <div class="artist-meta">
                        <a href="${a.imageUrl}" target="_blank" rel="noopener">Image source</a>
                      </div>
                    </c:if>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty a.imageUrl}">
                        <img src="${a.imageUrl}" alt="${a.name}" class="artist-img"/>
                      </c:when>
                      <c:otherwise>
                        <span class="artist-bio muted">No image</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty a.bio}">
                        <c:choose>
                          <c:when test="${fn:length(a.bio) > 220}">
                            <div class="artist-bio">${fn:substring(a.bio, 0, 220)}&hellip;</div>
                          </c:when>
                          <c:otherwise>
                            <div class="artist-bio">${a.bio}</div>
                          </c:otherwise>
                        </c:choose>
                      </c:when>
                      <c:otherwise>
                        <div class="artist-bio muted">No bio provided.</div>
                      </c:otherwise>
                    </c:choose>
                  </td>
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
