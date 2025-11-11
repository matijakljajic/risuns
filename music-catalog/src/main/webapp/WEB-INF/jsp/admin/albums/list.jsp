<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<h2>Albums</h2>
<p><a href="/admin">Admin home</a> | <a href="/admin/albums/new">New Album</a></p>

<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Title</th><th>Year</th><th>Artist</th><th>Actions</th></tr>
  <c:forEach items="${albums}" var="al">
    <tr>
      <td>${al.id}</td>
      <td>${al.title}</td>
      <td>${al.releaseYear}</td>
      <td><c:out value="${al.primaryArtist != null ? al.primaryArtist.name : ''}"/></td>
      <td>
        <a href="/admin/albums/${al.id}/edit">Edit</a>
        <form action="/admin/albums/${al.id}/delete" method="post" style="display:inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" onclick="return confirm('Delete?')">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body></html>
