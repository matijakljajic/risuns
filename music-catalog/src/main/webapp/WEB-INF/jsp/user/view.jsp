<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${profile.username}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .playlists-list {
      list-style: none;
      margin: 0;
      padding: 0;
      display: grid;
      gap: 10px;
    }
    .playlists-list li {
      border: 1px solid #e4e7ef;
      padding: 10px 12px;
    }
    .timeline {
      list-style: none;
      margin: 0;
      padding: 0;
      display: grid;
      gap: 8px;
    }
    .timeline li {
      border: 1px solid #e4e7ef;
      padding: 10px 12px;
    }
    .pager {
      display: flex;
      justify-content: space-between;
      margin-top: 12px;
    }
  </style>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="content-card">
      <h1>${profile.displayName != null ? profile.displayName : profile.username}</h1>
      <div class="meta-grid">
        <div class="meta-item"><strong>Username</strong><br>${profile.username}</div>
        <div class="meta-item"><strong>Role</strong><br>${profile.role}</div>
        <div class="meta-item"><strong>Email</strong><br><c:out value="${profile.email}"/></div>
      </div>

      <div style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
        <h3 style="margin:0;">Playlists</h3>
        <c:if test="${isSelf}">
          <a class="btn small" href="/users/${profile.id}/playlists/manage">Manage playlists</a>
        </c:if>
      </div>
      <p>
        Public: ${publicPlaylistCount}
        <c:if test="${isSelf}">
          · Private: ${privatePlaylistCount}
        </c:if>
      </p>
      <c:choose>
        <c:when test="${empty visiblePlaylists}">
          <p>${isSelf ? "You haven't" : "This user hasn't"} created any playlists yet.</p>
        </c:when>
        <c:otherwise>
          <ul class="playlists-list">
            <c:forEach items="${visiblePlaylists}" var="playlist">
              <li style="display: inline-flex; justify-content: space-between;">
                <a href="/playlists/${playlist.id}">${playlist.name}</a>
                <c:if test="${isSelf and not playlist['public']}">
                  <em>(Private)</em>
                </c:if>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>

      <h3>Last 7 days of listening</h3>
      <p>${weeklyListenCount} plays · ${weeklyUniqueTracks} unique tracks</p>

      <h4>Top 5 tracks this week</h4>
      <c:choose>
        <c:when test="${empty topTracks}">
          <p>No track has been played in the last 7 days.</p>
        </c:when>
        <c:otherwise>
          <ul class="stat-list">
            <c:forEach items="${topTracks}" var="stat">
              <li>
                ${stat.trackTitle}
                <small><a href="/albums/${stat.albumId}">${stat.albumTitle}</a> · ${stat.plays} plays</small>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>

      <h4>Listening timeline</h4>
      <c:choose>
        <c:when test="${empty weeklyListensPage}">
          <p>No listening activity recorded in the last 7 days.</p>
        </c:when>
        <c:otherwise>
          <p>Showing ${pageStart}–${pageEnd} of ${weeklyListenCount} plays</p>
          <div class="timeline">
            <c:forEach items="${weeklyListensPage}" var="listen">
              <c:set var="cover" value="${not empty listen.track.album.coverUrl ? listen.track.album.coverUrl : 'https://placehold.co/120x120?text=Album'}"/>
              <div class="track-row">
                <img src="${cover}" alt="${listen.track.album.title}"
                     onerror="this.onerror=null;this.src='https://placehold.co/120x120?text=Album';">
                <div>
                  <strong>${listen.track.title}</strong>
                  <div>
                    by <a href="/artists/${listen.track.album.primaryArtist.id}">${listen.track.album.primaryArtist.name}</a>
                    (<a href="/albums/${listen.track.album.id}">${listen.track.album.title}</a>)
                  </div>
                  <small>${listen.formattedTime}</small>
                </div>
              </div>
            </c:forEach>
          </div>
          <div class="pager">
            <div>
              <c:if test="${hasPrev}">
                <a href="?page=${page - 1}">← Newer</a>
              </c:if>
            </div>
            <div>
              <c:if test="${hasNext}">
                <a href="?page=${page + 1}">Older →</a>
              </c:if>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
