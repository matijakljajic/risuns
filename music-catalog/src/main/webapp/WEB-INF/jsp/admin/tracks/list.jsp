<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<h2>Tracks</h2>
<p><a href="/admin">Admin home</a> | <a href="/admin/tracks/new">New Track</a></p>

<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Title</th><th>#</th><th>Explicit</th><th>Album</th><th>Genres</th><th>Actions</th></tr>
  <c:forEach items="${tracks}" var="t">
    <tr>
      <td>${t.id}</td>
      <td>${t.title}</td>
      <td>${t.trackNo}</td>
      <td><c:out value="${t.explicit}"/></td>
      <td><c:out value="${t.album != null ? t.album.title : ''}"/></td>
      <td>
        <c:forEach items="${t.genres}" var="g" varStatus="st">
          ${g.name}<c:if test="${!st.last}">, </c:if>
        </c:forEach>
      </td>
      <td>
        <a href="/admin/tracks/${t.id}/edit">Edit</a>
        <form action="/admin/tracks/${t.id}/delete" method="post" style="display:inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" onclick="return confirm('Delete?')">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body></html>
