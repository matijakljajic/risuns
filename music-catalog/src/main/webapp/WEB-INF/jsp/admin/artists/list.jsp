<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<h2>Artists</h2>
<p><a href="/admin">Admin home</a> | <a href="/admin/artists/new">New Artist</a></p>

<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Name</th><th>Image URL</th><th>Actions</th></tr>
  <c:forEach items="${artists}" var="a">
    <tr>
      <td>${a.id}</td>
      <td>${a.name}</td>
      <td><c:out value="${a.imageUrl}"/></td>
      <td>
        <a href="/admin/artists/${a.id}/edit">Edit</a>
        <form action="/admin/artists/${a.id}/delete" method="post" style="display:inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" onclick="return confirm('Delete?')">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body></html>
