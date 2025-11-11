<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Music Catalog</title>
  <%@ include file="/WEB-INF/jsp/_site_header_styles.jspf" %>
  <style>
    body {
      font-family: "Segoe UI", Arial, sans-serif;
      margin: 0;
      background-color: #f7f7f7;
      color: #111;
    }
    main { padding: 24px; }

    .cards {
      display: grid;
      gap: 24px;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    }

    .info-card {
      background: #fff;
      border: 1px solid #e5e7eb;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
      padding: 20px 24px;
    }

    .info-card h3 {
      margin: 0 0 12px;
    }

    .genre-list,
    .listen-list {
      list-style: none;
      margin: 0;
      padding: 0;
      display: grid;
      gap: 8px;
    }

    .genre-chip {
      display: inline-flex;
      padding: 6px 10px;
      border: 1px solid #e5e7eb;
      background: #f4f5f7;
      text-decoration: none;
      color: #111;
      font-weight: 500;
    }

    .listen-item {
      border: 1px solid #e5e7eb;
      padding: 10px 12px;
      background: #fafafa;
    }

    .listen-item a {
      font-weight: 600;
      color: #111;
      text-decoration: none;
    }

    .listen-item a:hover,
    .genre-chip:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/jsp/_site_nav.jspf" %>

  <main>
    <div class="cards">
      <section class="info-card">
        <h3>Browse by genre</h3>
        <c:choose>
          <c:when test="${empty genres}">
            <p>No genres available yet.</p>
          </c:when>
          <c:otherwise>
            <ul class="genre-list">
              <c:forEach items="${genres}" var="genre">
                <li><a class="genre-chip" href="/search?type=tracks&genreId=${genre.id}">${genre.name}</a></li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </section>

      <section class="info-card">
        <h3>Latest listens</h3>
        <c:choose>
          <c:when test="${empty recentListens}">
            <p>No one has listened to anything yet. Come back soon!</p>
          </c:when>
          <c:otherwise>
            <ul class="listen-list">
              <c:forEach items="${recentListens}" var="listen">
                <li class="listen-item">
                  <a href="/users/${listen.userId}">${listen.username}</a>
                  listened to
                  <a href="/albums/${listen.albumId}">${listen.trackTitle}</a>
                  by
                  <a href="/artists/${listen.primaryArtist.id}">${listen.primaryArtist.name}</a>
                  <c:if test="${not empty listen.featuredArtists}">
                    feat.
                    <c:forEach items="${listen.featuredArtists}" var="feat" varStatus="loop">
                      <a href="/artists/${feat.id}">${feat.name}</a><c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:if>
                </li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </section>
    </div>
  </main>
</body>
</html>
