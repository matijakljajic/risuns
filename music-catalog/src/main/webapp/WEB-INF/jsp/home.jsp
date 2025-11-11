<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Music Catalog</title>
  <style>
    body { font-family: "Segoe UI", Arial, sans-serif; margin: 0; padding: 0; }
    header { background: #111; color: #fff; padding: 12px 24px; }
    .brand { font-size: 1.4rem; margin: 0; }
    .main-nav { width: 100%; margin-top: 8px; }
    .main-nav ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
      gap: 18px;
      width: 100%;
    }
    .main-nav a { color: #f5f5f5; text-decoration: none; }
    .main-nav a:hover { text-decoration: underline; }
    .logout-li {
      margin-left: auto; /* pushes this item to the far right */
    }
    form.inline { display: inline-flex; align-items: center; gap: 8px; }
    .logout-btn {
      border: none;
      background: #111;
      color: #f5f5f5;
      padding: 0;
      cursor: pointer;
      text-decoration: none;
      font-size: medium;
    }
    .logout-btn:hover {
      text-decoration: underline;
    }
    main { padding: 24px; }
    section { margin-bottom: 32px; }
  </style>
</head>
<body>
  <header>
    <h1 class="brand">Music Catalog</h1>
    <nav class="main-nav">
      <ul>
        <li><a href="/">Home</a></li>
        <li><a href="/search">Search</a></li>

        <sec:authorize access="isAnonymous()">
          <li><a href="/login">Login</a></li>
        </sec:authorize>

        <sec:authorize access="hasRole('ADMIN')">
          <li><a href="/admin">Dashboard</a></li>
          <li><a href="/h2-console">H2 Console</a></li>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
          <li><a href="/messages">Messages</a></li>
          <li class="logout-li">
            <form class="inline" action="/logout" method="post">
              <span>Signed in as <sec:authentication property="name"/></span><span>-</span>
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <button class="logout-btn" type="submit">Logout</button>
            </form>
          </li>
        </sec:authorize>
      </ul>
    </nav>
  </header>

  <main>
    <section>
      <h3>Browse by genre</h3>
      <c:choose>
        <c:when test="${empty genres}">
          <p>No genres available yet.</p>
        </c:when>
        <c:otherwise>
          <ul>
            <c:forEach items="${genres}" var="genre">
              <li><a href="/search?type=tracks&genreId=${genre.id}">${genre.name}</a></li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>
    </section>

    <section>
      <h3>Latest listens</h3>
      <c:choose>
        <c:when test="${empty recentListens}">
          <p>No one has listened to anything yet. Come back soon!</p>
        </c:when>
        <c:otherwise>
          <ul>
            <c:forEach items="${recentListens}" var="listen">
              <li>
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
  </main>
</body>
</html>
