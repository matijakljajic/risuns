<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${playlist.name}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="content-card">
      <h1>${playlist.name}</h1>
      <div class="meta-grid">
        <div class="meta-item">
          <strong>Created by</strong><br>
          <c:choose>
            <c:when test="${playlist.user != null}">
              <a href="/users/${playlist.user.id}">${playlist.user.username}</a>
            </c:when>
            <c:otherwise>
              Unknown user
            </c:otherwise>
          </c:choose>
        </div>
        <div class="meta-item">
          <strong>Created at</strong><br>
          <c:out value="${playlistCreatedAt != null ? playlistCreatedAt : 'Unknown'}"/>
        </div>
      </div>

      <h3>Tracks</h3>
      <c:choose>
        <c:when test="${empty items}">
          <p>This playlist has no tracks yet.</p>
        </c:when>
        <c:otherwise>
          <div class="track-list">
            <c:forEach items="${items}" var="item" varStatus="pos">
              <c:set var="track" value="${item.track}"/>
              <c:set var="cover" value="${not empty track.album.coverUrl ? track.album.coverUrl : 'https://placehold.co/120x120?text=Album'}"/>
              <div class="track-row">
                <img src="${cover}" alt="${track.album.title}"
                     onerror="this.onerror=null;this.src='https://placehold.co/120x120?text=Album';">
                <div>
                  <small>#${pos.index + 1}</small>
                  <strong>${track.title}</strong>
                  <div>
                    by <a href="/artists/${track.album.primaryArtist.id}">${track.album.primaryArtist.name}</a>
                    (<a href="/albums/${track.album.id}">${track.album.title}</a>)
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>

      <h3>Most active listeners</h3>
      <c:choose>
        <c:when test="${empty playlistTopListeners}">
          <p>No listening activity recorded yet.</p>
        </c:when>
        <c:otherwise>
          <ul class="stat-list">
            <c:forEach items="${playlistTopListeners}" var="listener">
              <li>
                <a href="/users/${listener.userId}">
                  <c:out value="${listener.label}"/>
                </a>
                <small>${listener.plays} plays</small>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
