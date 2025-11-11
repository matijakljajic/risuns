<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<c:set var="formAction" value="/admin/albums" />
<c:if test="${not empty album.id}">
  <c:set var="formAction" value="${formAction}/${album.id}" />
</c:if>
<h2><c:if test="${empty album.id}">New</c:if><c:if test="${not empty album.id}">Edit</c:if> Album</h2>
<form method="post" action="${formAction}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <p>
    <label>Title</label><br>
    <input type="text" name="title" value="<c:out value='${album.title}'/>" required maxlength="255">
  </p>
  <p>
    <label>Release Year</label><br>
    <input type="number" name="releaseYear" value="<c:out value='${album.releaseYear}'/>" min="1900" max="2100" required>
  </p>
  <p>
    <label>Primary Artist</label><br>
    <select name="primaryArtist.id" required>
      <c:forEach items="${artists}" var="a">
        <option value="${a.id}" <c:if test="${album.primaryArtist != null && album.primaryArtist.id == a.id}">selected</c:if>>
          ${a.name}
        </option>
      </c:forEach>
    </select>
  </p>
  <p>
    <label>Cover URL</label><br>
    <input type="url" name="coverUrl" value="<c:out value='${album.coverUrl}'/>">
  </p>
  <p><button type="submit">Save</button> <a href="/admin/albums">Cancel</a></p>
</form>

<c:if test="${not empty org.springframework.validation.BindingResult.album}">
  <div style="color:red">
    <c:forEach items="${org.springframework.validation.BindingResult.album.allErrors}" var="e">
      <div>${e.defaultMessage}</div>
    </c:forEach>
  </div>
</c:if>
</body></html>
