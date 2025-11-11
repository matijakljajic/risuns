<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>${profile.username} · Report</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .report-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
      gap: 16px;
      margin-bottom: 24px;
    }
    .report-card {
      border: 1px solid #e2e8f0;
      padding: 12px 14px;
      background: #f8fafc;
      border-radius: 6px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 12px;
    }
    th, td {
      text-align: left;
      padding: 8px 6px;
      border-bottom: 1px solid #e2e8f0;
    }
    th {
      background: #f1f5f9;
      font-weight: 600;
      font-size: 0.9rem;
    }
    .actions {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
      margin-bottom: 20px;
    }
    .top-tracks {
      list-style: decimal inside;
      padding-left: 0;
    }
    .top-tracks li {
      margin-bottom: 6px;
    }
  </style>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/users/${profile.id}">← Back to profile</a>
    <div class="content-card">
      <div style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
        <div>
          <h1 style="margin-bottom:4px;">${profile.displayName != null ? profile.displayName : profile.username}</h1>
          <p style="margin:0; color:#475569;">Generated at ${generatedAt}</p>
        </div>
        <div class="actions">
          <a class="btn" href="${downloadUrl}">Download PDF</a>
        </div>
      </div>
      <p style="margin-top:12px;">
        This report summarizes your playlists and listening highlights from the past seven days (since ${weeklySinceLabel}).
      </p>

      <div class="report-grid">
        <div class="report-card">
          <strong>Public playlists</strong><br>
          ${publicPlaylistCount}
        </div>
        <div class="report-card">
          <strong>Private playlists</strong><br>
          ${privatePlaylistCount}
        </div>
        <div class="report-card">
          <strong>Weekly plays</strong><br>
          ${weeklyListenCount}
        </div>
        <div class="report-card">
          <strong>Unique tracks</strong><br>
          ${weeklyUniqueTracks}
        </div>
      </div>

      <h3 style="margin-top:0;">Playlists overview</h3>
      <c:choose>
        <c:when test="${empty playlistSummaries}">
          <p>You do not have any playlists yet.</p>
        </c:when>
        <c:otherwise>
          <table>
            <thead>
              <tr>
                <th style="width:45%;">Name</th>
                <th style="width:15%;">Visibility</th>
                <th style="width:15%;">Tracks</th>
                <th>Created</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${playlistSummaries}" var="playlist">
                <tr>
                  <td>
                    <a href="/playlists/${playlist.id}"><c:out value="${playlist.name}"/></a>
                  </td>
                  <td>
                    <c:out value="${playlist.visibility}"/>
                  </td>
                  <td>
                    ${playlist.trackCount}
                  </td>
                  <td>
                    <c:out value="${playlist.createdAtLabel}"/>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>

      <h3 style="margin-top:32px;">Top tracks · last 7 days</h3>
      <c:choose>
        <c:when test="${empty topTracks}">
          <p>No listening activity recorded in this period.</p>
        </c:when>
        <c:otherwise>
          <ol class="top-tracks">
            <c:forEach items="${topTracks}" var="stat">
              <li>
                <c:out value="${stat.trackTitle != null ? stat.trackTitle : 'Unknown track'}"/>
                <small>
                  · <c:out value="${stat.albumTitle != null ? stat.albumTitle : 'Unknown album'}"/>
                  (${stat.plays} plays)
                </small>
              </li>
            </c:forEach>
          </ol>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
