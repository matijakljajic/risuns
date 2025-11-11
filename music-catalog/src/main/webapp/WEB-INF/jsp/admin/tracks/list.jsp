<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Tracks</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .track-title {
      font-weight: 600;
      margin-bottom: 4px;
    }
    .track-meta {
      font-size: 0.85rem;
      color: #64748b;
    }
    .track-album {
      display: flex;
      gap: 12px;
      align-items: center;
    }
    .track-album img {
      width: 56px;
      height: 56px;
      object-fit: cover;
      border: 1px solid #e2e8f0;
    }
    .track-flags {
      display: flex;
      flex-direction: column;
      gap: 4px;
      font-size: 0.85rem;
    }
  </style>
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
      <form class="admin-filter" method="get">
        <div class="filter-field">
          <label for="track-q">Search</label>
          <input id="track-q" type="text" name="q" value="${query}" placeholder="Search by title"/>
        </div>
        <div class="filter-field">
          <label for="track-genre">Genre</label>
          <select id="track-genre" name="genreId">
            <option value="">All genres</option>
            <c:forEach items="${genres}" var="g">
              <option value="${g.id}" <c:if test="${genreId ne null and genreId eq g.id}">selected</c:if>>
                ${g.name}
              </option>
            </c:forEach>
          </select>
        </div>
        <div class="filter-field">
          <label for="track-explicit">Explicit</label>
          <select id="track-explicit" name="explicit">
            <option value="">Any</option>
            <option value="explicit" <c:if test="${explicitFilter == 'explicit'}">selected</c:if>>Explicit only</option>
            <option value="clean" <c:if test="${explicitFilter == 'clean'}">selected</c:if>>Clean only</option>
          </select>
        </div>
        <button class="btn btn-secondary" type="submit">Apply</button>
        <c:if test="${not empty query or genreId ne null or (explicitFilter ne null and explicitFilter ne '')}">
          <a class="btn-link" href="/admin/tracks">Reset</a>
        </c:if>
      </form>
      <c:choose>
        <c:when test="${empty tracks}">
          <div class="empty-state">
            No tracks recorded yet. <a href="/admin/tracks/new">Add your first track</a>.
          </div>
        </c:when>
        <c:otherwise>
          <table class="admin-table">
            <thead>
              <tr><th>ID</th><th>Track</th><th>Album</th><th>Genres</th><th>Flags</th><th>Actions</th></tr>
            </thead>
            <tbody>
              <c:forEach items="${tracks}" var="t">
                <tr>
                  <td>${t.id}</td>
                  <td>
                    <div class="track-title">${t.title}</div>
                    <div class="track-meta">Track #${t.trackNo}</div>
                  </td>
                  <td>
                    <div class="track-album">
                      <c:choose>
                        <c:when test="${t.album != null && not empty t.album.coverUrl}">
                          <img src="${t.album.coverUrl}" alt="${t.album.title}"/>
                        </c:when>
                        <c:otherwise>
                          <div class="track-meta">No cover</div>
                        </c:otherwise>
                      </c:choose>
                      <div>
                        <div class="track-title" style="font-size:0.95rem;">
                          <c:out value="${t.album != null ? t.album.title : 'No album'}"/>
                        </div>
                        <div class="track-meta">
                          <c:if test="${t.album != null && t.album.primaryArtist != null}">
                            ${t.album.primaryArtist.name}
                          </c:if>
                        </div>
                      </div>
                    </div>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty t.genres}">
                        <div style="display:flex;flex-wrap:wrap;gap:6px;">
                          <c:forEach items="${t.genres}" var="g">
                            <span class="admin-chip">${g.name}</span>
                          </c:forEach>
                        </div>
                      </c:when>
                      <c:otherwise>
                        <span class="track-meta">No genres</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <div class="track-flags">
                      <span>
                        <strong>Explicit:</strong>
                        <c:choose>
                          <c:when test="${t.explicit}">Yes</c:when>
                          <c:otherwise>No</c:otherwise>
                        </c:choose>
                      </span>
                      <c:if test="${t.album != null}">
                        <span><strong>Album ID:</strong> ${t.album.id}</span>
                      </c:if>
                    </div>
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
