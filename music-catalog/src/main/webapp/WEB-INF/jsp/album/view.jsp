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
                  <div class="track-row">
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
                    <c:if test="${track.explicit}">
                      <span class="pill">Explicit</span>
                    </c:if>
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
    </div>
  </div>
</body>
</html>
