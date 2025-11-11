<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin Dashboard</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .dashboard-hero {
      padding: 28px;
      margin-bottom: 28px;
      border: 1px solid #e4e7ef;
      background: linear-gradient(135deg, #eef2ff, #f8fafc);
      display: flex;
      flex-wrap: wrap;
      gap: 24px;
      align-items: center;
    }

    .dashboard-hero h2 {
      margin: 0 0 8px;
      font-size: 1.6rem;
    }

    .dashboard-hero p {
      margin: 0;
      color: #475569;
      max-width: 520px;
      line-height: 1.5;
    }

    .dashboard-grid {
      display: grid;
      gap: 18px;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    }

    .dashboard-card {
      border: 1px solid #e4e7ef;
      padding: 20px 22px;
      background: #fff;
      display: flex;
      flex-direction: column;
      gap: 10px;
      min-height: 160px;
    }

    .dashboard-card__title {
      font-size: 1.05rem;
      font-weight: 600;
      margin: 0;
    }

    .dashboard-card__body {
      flex: 1;
      color: #475569;
      font-size: 0.95rem;
      line-height: 1.4;
    }

    .dashboard-card__actions {
      margin-top: auto;
    }

    .link-list {
      list-style: none;
      padding: 0;
      margin: 0;
      display: grid;
      gap: 6px;
      font-size: 0.92rem;
    }

    .link-list a {
      color: #0f172a;
    }

    .dashboard-section {
      margin-top: 36px;
    }

    .dashboard-section h3 {
      margin: 0 0 12px;
      font-size: 1.1rem;
      letter-spacing: 0.03em;
      text-transform: uppercase;
      color: #475569;
    }
  </style>
</head>
<body>
  <c:set var="pageTitle" value="Dashboard"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="dashboard-hero">
        <div>
          <h2>Welcome to the Music Catalog control room</h2>
          <p>Use the shortcuts below to curate artists, albums, and tracks or to moderate the community. Each area opens the dedicated admin view with rich filters and editing tools.</p>
        </div>
        <a class="btn btn-primary" href="/admin/users">Review users</a>
      </div>

      <div class="dashboard-section">
        <h3>Catalog management</h3>
        <div class="dashboard-grid">
          <div class="dashboard-card">
            <div class="dashboard-card__title">Genres</div>
            <div class="dashboard-card__body">Edit or create genres that keep the search filters tidy. Great place to start before adding new artists.</div>
            <div class="dashboard-card__actions">
              <a class="btn btn-secondary" href="/admin/genres">Manage genres</a>
            </div>
          </div>

          <div class="dashboard-card">
            <div class="dashboard-card__title">Artists</div>
            <div class="dashboard-card__body">Update artwork, bios, and identities. Artists power album pages, so keep them fresh.</div>
            <div class="dashboard-card__actions">
              <a class="btn btn-secondary" href="/admin/artists">Manage artists</a>
            </div>
          </div>

          <div class="dashboard-card">
            <div class="dashboard-card__title">Albums</div>
            <div class="dashboard-card__body">Curate discographies, release years, and cover art. Albums feed both public and admin search.</div>
            <div class="dashboard-card__actions">
              <a class="btn btn-secondary" href="/admin/albums">Manage albums</a>
            </div>
          </div>

          <div class="dashboard-card">
            <div class="dashboard-card__title">Tracks</div>
            <div class="dashboard-card__body">Control track ordering, explicit flags, genres, and featured artists. Perfect for quick metadata fixes.</div>
            <div class="dashboard-card__actions">
              <a class="btn btn-secondary" href="/admin/tracks">Manage tracks</a>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboard-section">
        <h3>Community & access</h3>
        <div class="dashboard-grid">
          <div class="dashboard-card">
            <div class="dashboard-card__title">User accounts</div>
            <div class="dashboard-card__body">Reset passwords, toggle notification preferences, and change roles for admins or regular listeners.</div>
            <div class="dashboard-card__actions">
              <a class="btn btn-secondary" href="/admin/users">Manage users</a>
            </div>
          </div>
          <div class="dashboard-card">
            <div class="dashboard-card__title">Quick links</div>
            <div class="dashboard-card__body">
              <ul class="link-list">
                <li><a href="/">View the public homepage</a></li>
                <li><a href="/search">Open catalog search</a></li>
                <li><a href="/messages">Jump to messages</a></li>
                <li><a href="/h2-console" target="_blank" rel="noopener">H2 console (dev only)</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</body>
</html>
