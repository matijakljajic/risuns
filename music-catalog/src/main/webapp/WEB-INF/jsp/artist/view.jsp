<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${artist.name}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="content-card">
      <style>
        .artist-hero {
          display: flex;
          gap: 24px;
          flex-wrap: wrap;
          align-items: center;
        }
        .artist-hero img {
          width: 220px;
          height: 220px;
          object-fit: cover;
          border: 1px solid #dfe4ef;
        }
        .artist-hero h1 {
          margin-top: 0;
          margin-bottom: 12px;
        }
      </style>
      <c:set var="artistImage" value="${not empty artist.imageUrl ? artist.imageUrl : 'https://placehold.co/400x400?text=Artist'}"/>
      <div class="artist-hero">
        <img src="${artistImage}"
             alt="${artist.name}"
             onerror="this.onerror=null;this.src='https://placehold.co/400x400?text=Artist';">
        <div>
          <h1>${artist.name}</h1>
          <p><c:out value="${artist.bio != null ? artist.bio : 'No biography yet.'}"/></p>
        </div>
      </div>

      <h3>Albums</h3>
      <c:choose>
        <c:when test="${empty albums}">
          <p>No albums recorded yet.</p>
        </c:when>
        <c:otherwise>
          <ul class="list-block">
            <c:forEach items="${albums}" var="album">
              <li>
                <a href="/albums/${album.id}">${album.title}</a>
                <c:if test="${album.releaseYear != null}">(${album.releaseYear})</c:if>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
