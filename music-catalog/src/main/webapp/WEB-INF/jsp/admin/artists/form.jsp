<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<c:set var="formAction" value="/admin/artists" />
<c:if test="${not empty artist.id}">
  <c:set var="formAction" value="${formAction}/${artist.id}" />
</c:if>
<h2><c:if test="${empty artist.id}">New</c:if><c:if test="${not empty artist.id}">Edit</c:if> Artist</h2>
<form method="post" action="${formAction}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <p>
    <label>Name</label><br>
    <input type="text" name="name" value="<c:out value='${artist.name}'/>" required maxlength="255">
  </p>
  <p>
    <label>Image URL</label><br>
    <input type="url" name="imageUrl" value="<c:out value='${artist.imageUrl}'/>">
  </p>
  <p>
    <label>Bio</label><br>
    <textarea name="bio" rows="5" cols="60"><c:out value='${artist.bio}'/></textarea>
  </p>
  <p><button type="submit">Save</button> <a href="/admin/artists">Cancel</a></p>
</form>

<c:if test="${not empty org.springframework.validation.BindingResult.artist}">
  <div style="color:red">
    <c:forEach items="${org.springframework.validation.BindingResult.artist.allErrors}" var="e">
      <div>${e.defaultMessage}</div>
    </c:forEach>
  </div>
</c:if>
</body></html>
