<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Manage playlists · ${owner.username}</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .form-grid {
      display: grid;
      gap: 12px;
      border: 1px solid #e4e7ef;
      padding: 16px;
      background: #f8fafc;
    }
    .playlist-manage-row {
      border: 1px solid #e4e7ef;
      padding: 12px;
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      align-items: center;
    }
    .playlist-manage-row form {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
      align-items: center;
    }
    input[type="text"] {
      border: 1px solid #cbd5f5;
      padding: 6px 8px;
      min-width: 200px;
    }
  </style>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/users/${owner.id}">← Back to profile</a>
    <div class="content-card">
      <h1>Manage playlists for ${owner.username}</h1>
      <c:if test="${not empty message}">
        <div class="flash">${message}</div>
      </c:if>

      <h3>Create a new playlist</h3>
      <form class="form-grid" method="post" action="/users/${owner.id}/playlists">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <label>
          Name<br>
          <input type="text" name="name" required maxlength="255" placeholder="My playlist">
        </label>
        <label style="display:flex; align-items:center; gap:8px;">
          <input type="checkbox" name="public" value="true">
          Public
        </label>
        <button type="submit" class="btn">Create</button>
      </form>

      <h3 style="margin-top:32px;">Your playlists</h3>
      <c:choose>
        <c:when test="${empty playlists}">
          <p>No playlists yet. Use the form above to create one.</p>
        </c:when>
        <c:otherwise>
          <div class="stack" style="display:grid; gap:12px;">
            <c:forEach items="${playlists}" var="playlist">
              <div class="playlist-manage-row">
                <form method="post" action="/users/${owner.id}/playlists/${playlist.id}/update">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <input type="text" name="name" value="${playlist.name}" required maxlength="255">
                  <label style="display:flex; align-items:center; gap:6px;">
                    <input type="checkbox" name="public" value="true"
                      <c:if test="${playlist['public']}">checked</c:if>>
                    Public
                  </label>
                  <button type="submit" class="btn small">Save</button>
                </form>
                <form method="post" action="/users/${owner.id}/playlists/${playlist.id}/delete"
                      onsubmit="return confirm('Delete ${playlist.name}?');">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <button type="submit" class="btn danger">Delete</button>
                </form>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
