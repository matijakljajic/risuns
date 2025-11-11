<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Search</title>
  <%@ include file="/WEB-INF/jsp/_site_header_styles.jspf" %>
  <style>
    body {
      font-family: "Segoe UI", Arial, sans-serif;
      margin: 0;
      background-color: #f7f7f7;
      color: #111;
    }
    main { padding: 24px; }

    h2 {
      margin-top: 0;
      margin-bottom: 16px;
      font-weight: 600;
    }

    .search-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px 24px;
      align-items: flex-end;
      padding: 16px 20px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.06);
      margin-bottom: 24px;
      border: 1px solid #eee;
    }

    label {
      display: flex;
      flex-direction: column;
      font-size: 0.95rem;
      color: #333;
      min-width: 180px;
    }

    select,
    input[type="text"],
    input[type="number"] {
      margin-top: 4px;
      padding: 6px 8px;
      border: 1px solid #ccc;
      font-size: 0.95rem;
      border-radius: 0;
    }

    button[type="submit"] {
      padding: 8px 18px;
      border: none;
      background: #111;
      color: #f5f5f5;
      font-size: 0.95rem;
      cursor: pointer;
      border-radius: 0;
    }

    button[type="submit"]:hover {
      background: #222;
    }

    .filter-block {
      display: none;
      flex-direction: row;
      gap: 12px;
      min-width: 200px;
    }

    .filter-block.is-visible {
      display: flex;
    }

    h3 {
      margin-top: 0;
      margin-bottom: 8px;
    }

    h4 {
      margin: 0 0 10px 0;
      font-size: 1rem;
      font-weight: 600;
    }

    a {
      color: #111;
      text-decoration: none;
      font-weight: 500;
    }

    a:hover {
      text-decoration: underline;
    }

    .back-link {
      display: inline-block;
      margin-top: 24px;
      font-size: 0.9rem;
    }

    /* Results layout */
    .results-section {
      margin-top: 8px;
    }

    .results-columns {
      display: grid;
      width: 100%;
      gap: 16px;
      margin-top: 12px;
      grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    }

    .results-column {
      background: #ffffff;
      padding: 10px 10px 8px;
      box-shadow: 0 1px 4px rgba(0,0,0,0.04);
      border: 1px solid #eee;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .result-block {
      padding: 6px 6px 4px;
      border-radius: 0;
      transition: background 0.15s ease, box-shadow 0.15s ease, transform 0.1s ease;
    }

    .result-block:hover {
      background: #f5f5f5;
      box-shadow: 0 1px 6px rgba(0,0,0,0.06);
      transform: translateY(-1px);
    }

    .result-main-link {
      display: inline-block;
      font-size: 0.95rem;
      font-weight: 600;
      padding-top: 4px;
      padding-bottom: 2px;
      padding-left: 6px;
      padding-right: 6px;
      background: #111;
      color: #f5f5f5;
      margin-bottom: 2px;
      border-radius: 0;
    }

    .result-main-link:hover {
      background: #222;
      text-decoration: none;
    }

    .result-meta {
      font-size: 0.85rem;
      color: #444;
      margin-top: 2px;
    }

    .result-meta a {
      font-weight: 500;
    }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/jsp/_site_nav.jspf" %>

  <main>
    <h2>Search the Catalog</h2>
    <form class="search-form" method="get" action="/search">
      <label>
        Search type
        <select name="type" data-type-select>
          <option value="general" <c:if test="${searchType == 'general'}">selected</c:if>>General</option>
          <option value="tracks" <c:if test="${searchType == 'tracks'}">selected</c:if>>Tracks</option>
          <option value="artists" <c:if test="${searchType == 'artists'}">selected</c:if>>Artists</option>
          <option value="albums" <c:if test="${searchType == 'albums'}">selected</c:if>>Albums</option>
          <option value="users" <c:if test="${searchType == 'users'}">selected</c:if>>Users</option>
        </select>
      </label>

      <label>
        Keywords
        <input type="text" name="q" value="<c:out value='${query}'/>" placeholder="Track title..." />
      </label>

      <div class="filter-block ${searchType == 'tracks' ? "is-visible" : ""}" data-filter="tracks">
        <label>
          Genre
          <select name="genreId">
            <option value="">All genres</option>
            <c:forEach items="${genres}" var="genre">
              <option value="${genre.id}" <c:if test="${genre.id == selectedGenreId}">selected</c:if>>
                ${genre.name}
              </option>
            </c:forEach>
          </select>
        </label>
        <label>
          Explicit
          <select name="explicit">
            <option value="">All tracks</option>
            <option value="true" <c:if test="${explicitFilter == 'true'}">selected</c:if>>Explicit only</option>
            <option value="false" <c:if test="${explicitFilter == 'false'}">selected</c:if>>Clean only</option>
          </select>
        </label>
      </div>

      <div class="filter-block ${searchType == 'albums' ? "is-visible" : ""}" data-filter="albums">
        <label>
          Release year
          <input type="number" name="releaseYear" min="1900" max="2100" value="<c:out value='${releaseYear}'/>" />
        </label>
      </div>

      <div class="filter-block ${searchType == 'users' ? "is-visible" : ""}" data-filter="users">
        <label>
          Role
          <select name="role">
            <option value="">All roles</option>
            <c:forEach items="${roles}" var="roleOption">
              <option value="${roleOption}" <c:if test="${roleFilter == roleOption}">selected</c:if>>
                ${roleOption}
              </option>
            </c:forEach>
          </select>
        </label>
      </div>

      <div class="actions">
        <button class="btn" type="submit">Search</button>
      </div>
    </form>

  <c:if test="${searched}">
    <div class="results-section">
      <h3>Results</h3>

      <c:if test="${noResults}">
        <p>No matches found.</p>
      </c:if>

      <div class="results-columns">
        <!-- Tracks -->
        <c:if test="${not empty trackResults}">
          <div class="results-column">
            <h4>Tracks</h4>
            <c:forEach items="${trackResults}" var="track">
              <div class="result-block">
                <a class="result-main-link" href="/albums/${track.albumId}">
                  ${track.trackTitle}
                </a>
                <div class="result-meta">
                  by
                  <a href="/artists/${track.primaryArtist.id}">${track.primaryArtist.name}</a>
                  <c:if test="${not empty track.featuredArtists}">
                    feat.
                    <c:forEach items="${track.featuredArtists}" var="feature" varStatus="loop">
                      <a href="/artists/${feature.id}">${feature.name}</a><c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:if>
                  <br/>
                  Album:
                  <a href="/albums/${track.albumId}">${track.albumTitle}</a>
                  <c:if test="${not empty track.genres}">
                    | Genres:
                    <c:forEach items="${track.genres}" var="g" varStatus="loop">
                      <a href="/search?genreId=${g.id}">${g.name}</a><c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:if>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:if>

        <!-- Albums -->
        <c:if test="${not empty albumResults}">
          <div class="results-column">
            <h4>Albums</h4>
            <c:forEach items="${albumResults}" var="album">
              <div class="result-block">
                <a class="result-main-link" href="/albums/${album.id}">${album.title}</a>
                <c:if test="${album.primaryArtistId != null}">
                  <div class="result-meta">
                    by <a href="/artists/${album.primaryArtistId}">${album.primaryArtistName}</a>
                  </div>
                </c:if>
              </div>
            </c:forEach>
          </div>
        </c:if>

        <!-- Playlists -->
        <c:if test="${not empty playlistResults}">
          <div class="results-column">
            <h4>Playlists</h4>
            <c:forEach items="${playlistResults}" var="playlist">
              <div class="result-block">
                <a class="result-main-link" href="/playlists/${playlist.id}">${playlist.name}</a>
                <c:if test="${playlist.ownerId != null}">
                  <div class="result-meta">
                    by <a href="/users/${playlist.ownerId}">${playlist.ownerUsername}</a>
                  </div>
                </c:if>
              </div>
            </c:forEach>
          </div>
        </c:if>

        <!-- Artists -->
        <c:if test="${not empty artistResults}">
          <div class="results-column">
            <h4>Artists</h4>
            <c:forEach items="${artistResults}" var="artist">
              <div class="result-block">
                <a class="result-main-link" href="/artists/${artist.id}">${artist.name}</a>
              </div>
            </c:forEach>
          </div>
        </c:if>

        <!-- Users -->
        <c:if test="${not empty userResults}">
          <div class="results-column">
            <h4>Users</h4>
            <c:forEach items="${userResults}" var="user">
              <div class="result-block">
                <a class="result-main-link" href="/users/${user.id}">${user.username}</a>
                <c:if test="${not empty user.displayName}">
                  <div class="result-meta">
                    ${user.displayName}
                  </div>
                </c:if>
              </div>
            </c:forEach>
          </div>
        </c:if>
      </div>
    </div>
  </c:if>
  </main>

  <script>
    (function(){
      const typeSelect = document.querySelector('[data-type-select]');
      const filterBlocks = document.querySelectorAll('[data-filter]');
      function syncFilters(){
        const val = typeSelect ? typeSelect.value : 'general';
        filterBlocks.forEach(block => {
          block.classList.toggle('is-visible', block.getAttribute('data-filter') === val);
        });
      }
      if (typeSelect) {
        typeSelect.addEventListener('change', syncFilters);
        syncFilters();
      }
    })();
  </script>

</body>
</html>
