<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${playlist.name}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .track-row {
      flex-wrap: wrap;
      align-items: center;
    }
    .track-row-controls {
      display: flex;
      gap: 6px;
      margin-left: auto;
      flex-wrap: wrap;
    }
    dialog {
      border: 1px solid #0f172a;
      padding: 20px 24px;
      width: min(420px, 90vw);
    }
    dialog::backdrop {
      background: rgba(15, 23, 42, 0.45);
    }
    .dialog-actions {
      display: flex;
      gap: 8px;
      justify-content: flex-end;
      margin-top: 16px;
    }
    .inline-form {
      display: inline;
    }
  </style>
</head>
<body>
  <c:set var="playlistLink" value="/playlists/${playlist.id}"/>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="content-card">
      <c:if test="${not empty message}">
        <div class="flash">${message}</div>
      </c:if>
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
                <div class="track-row-controls">
                  <c:if test="${canManage}">
                    <form class="inline-form" method="post"
                          action="/playlists/${playlist.id}/items/${item.id}/move">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                      <input type="hidden" name="direction" value="up"/>
                      <input type="hidden" name="redirect" value="${playlistLink}"/>
                      <button class="btn small ghost"
                              <c:if test="${pos.first}">disabled</c:if>>Move up</button>
                    </form>
                    <form class="inline-form" method="post"
                          action="/playlists/${playlist.id}/items/${item.id}/move">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                      <input type="hidden" name="direction" value="down"/>
                      <input type="hidden" name="redirect" value="${playlistLink}"/>
                      <button class="btn small ghost"
                              <c:if test="${pos.last}">disabled</c:if>>Move down</button>
                    </form>
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
      <c:if test="${not empty myPlaylists}">
        <dialog id="addToPlaylistDialog">
          <form method="post" action="/playlists/actions/add-track">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="trackId" id="dialogTrackId"/>
            <input type="hidden" name="redirect" value="${playlistLink}"/>
            <div>
              <strong>Add track to playlist</strong>
              <p id="dialogTrackLabel" style="margin: 8px 0 12px;"></p>
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
            const dialog = document.getElementById('addToPlaylistDialog');
            if (!dialog) return;
            const trackIdInput = document.getElementById('dialogTrackId');
            const trackLabel = document.getElementById('dialogTrackLabel');
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
