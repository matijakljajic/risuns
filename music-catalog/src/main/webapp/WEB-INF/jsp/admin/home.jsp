<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin Dashboard</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
  <style>
    .admin-grid {
      list-style: none;
      margin: 0;
      padding: 0;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 18px;
    }

    .admin-grid li {
      border: 1px solid #e4e7ef;
      padding: 18px 20px;
      background: #f8fafc;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .admin-grid span {
      font-size: 1rem;
      font-weight: 600;
    }
  </style>
</head>
<body>
  <c:set var="pageTitle" value="Dashboard"/>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2>Admin dashboard</h2>
          <p>Manage catalog entities and user accounts.</p>
        </div>
      </div>
      <ul class="admin-grid">
        <li>
          <span>Genres</span>
          <a class="btn btn-primary" href="/admin/genres">Open</a>
        </li>
        <li>
          <span>Artists</span>
          <a class="btn btn-primary" href="/admin/artists">Open</a>
        </li>
        <li>
          <span>Albums</span>
          <a class="btn btn-primary" href="/admin/albums">Open</a>
        </li>
        <li>
          <span>Tracks</span>
          <a class="btn btn-primary" href="/admin/tracks">Open</a>
        </li>
        <li>
          <span>Users</span>
          <a class="btn btn-primary" href="/admin/users">Open</a>
        </li>
      </ul>
    </div>
  </main>
</body>
</html>
