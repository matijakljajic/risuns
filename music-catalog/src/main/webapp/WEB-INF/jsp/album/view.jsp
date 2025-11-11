<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${album.title}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
</head>
<body>
  <c:set var="albumLink" value="/albums/${album.id}"/>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="content-card">
      <style>
        .album-hero {
          display: flex;
          flex-wrap: wrap;
          gap: 24px;
          align-items: flex-start;
        }
        .album-hero img {
          width: 260px;
          height: 260px;
          object-fit: cover;
        }
        .album-meta {
          flex: 1;
          min-width: 260px;
        }
      </style>
      <c:set var="albumCover" value="${not empty album.coverUrl ? album.coverUrl : 'https://placehold.co/600x600?text=Album+Cover'}"/>
      <div class="album-hero">
        <img class="hero-img"
             src="${albumCover}"
             alt="${album.title}"
             onerror="this.onerror=null;this.src='https://placehold.co/600x600?text=Album+Cover';">
        <div class="album-meta">
          <h1>${album.title}</h1>
          <div class="meta-grid">
            <div class="meta-item">
              <strong>Artist</strong><br>
              <a href="/artists/${album.primaryArtist.id}">${album.primaryArtist.name}</a>
            </div>
            <div class="meta-item">
              <strong>Release year</strong><br>
              <c:out value="${album.releaseYear != null ? album.releaseYear : 'N/A'}"/>
            </div>
          </div>
          <h3>Tracks</h3>
          <c:choose>
            <c:when test="${empty tracks}">
              <p>No tracks have been added yet.</p>
            </c:when>
            <c:otherwise>
              <div class="track-list">
                <c:forEach items="${tracks}" var="track" varStatus="loop">
                  <div class="track-row" style="gap:18px;">
                    <div>
                      <small>#<c:out value="${track.trackNo != null ? track.trackNo : loop.index + 1}"/></small>
                      <span>
                        <strong>${track.title}</strong>
                        <c:if test="${not empty track.features}">
                          feat.
                          <c:forEach items="${track.features}" var="feature" varStatus="featLoop">
                            <a href="/artists/${feature.artist.id}">${feature.artist.name}</a><c:if test="${!featLoop.last}">, </c:if>
                          </c:forEach>    
                        </c:if>
                      </span>
                    </div>
                    <div style="margin-left:auto; display:flex; gap:8px; align-items:center; flex-wrap:wrap;">
                      <c:if test="${track.explicit}">
                        <span class="pill">Explicit</span>
                      </c:if>
                      <c:if test="${not empty myPlaylists}">
                        <button type="button"
                                class="btn small tertiary add-to-playlist-btn"
                                data-track="${track.id}"
                                data-track-title="${track.title}"
                                aria-label="Add to playlist">
                          +
                        </button>
                      </c:if>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <h3>Most active listeners</h3>
      <c:choose>
        <c:when test="${empty albumTopListeners}">
          <p>No one has listened to this album yet.</p>
        </c:when>
        <c:otherwise>
          <ul class="stat-list">
            <c:forEach items="${albumTopListeners}" var="listener">
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
      <c:if test="${not empty myPlaylists}">
        <dialog id="albumAddToPlaylistDialog">
          <form method="post" action="/playlists/actions/add-track">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="trackId" id="albumDialogTrackId"/>
            <input type="hidden" name="redirect" value="${albumLink}"/>
            <div>
              <strong>Add track to playlist</strong>
              <p id="albumDialogTrackLabel" style="margin: 8px 0 12px;"></p>
            </div>
            <label style="display:block; font-weight:600; margin-bottom:6px;">Choose playlist</label>
            <select name="targetPlaylistId" required style="width:100%; padding:6px; border:1px solid #cbd5f5;">
              <c:forEach items="${myPlaylists}" var="owned">
                <option value="${owned.id}">
                  ${owned.name}<c:if test="${not owned['public']}"> (Private)</c:if>
                </option>
              </c:forEach>
            </select>
            <div class="dialog-actions">
              <button type="submit" class="btn">Add</button>
              <button type="button" class="btn secondary" data-close>Cancel</button>
            </div>
          </form>
        </dialog>
        <script>
          (function() {
            const dialog = document.getElementById('albumAddToPlaylistDialog');
            if (!dialog) return;
            const trackIdInput = document.getElementById('albumDialogTrackId');
            const trackLabel = document.getElementById('albumDialogTrackLabel');
            document.querySelectorAll('.add-to-playlist-btn').forEach(function(btn) {
              btn.addEventListener('click', function() {
                trackIdInput.value = btn.dataset.track;
                trackLabel.textContent = '“' + btn.dataset.trackTitle + '”';
                dialog.showModal();
              });
            });
            dialog.querySelectorAll('[data-close]').forEach(function(btn) {
              btn.addEventListener('click', function() {
                dialog.close();
              });
            });
          })();
        </script>
      </c:if>
    </div>
  </div>
</body>
</html>
