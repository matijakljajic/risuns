<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<h2>Genres</h2>
<p><a href="/admin">Admin home</a> | <a href="/admin/genres/new">New Genre</a></p>
<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Name</th><th>Actions</th></tr>
  <c:forEach items="${genres}" var="g">
    <tr>
      <td>${g.id}</td>
      <td>${g.name}</td>
      <td>
        <a href="/admin/genres/${g.id}/edit">Edit</a>
        <form action="/admin/genres/${g.id}/delete" method="post" style="display:inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" onclick="return confirm('Delete?')">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body></html>
