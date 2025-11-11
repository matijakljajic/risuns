<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Artist</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Artists"/>
  <c:set var="formAction" value="/admin/artists"/>
  <c:set var="isNew" value="${empty artist.id}"/>
  <c:if test="${not empty artist.id}">
    <c:set var="formAction" value="${formAction}/${artist.id}"/>
  </c:if>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2><c:choose><c:when test="${isNew}">Create artist</c:when><c:otherwise>Edit artist</c:otherwise></c:choose></h2>
          <p>${isNew ? "Add a new artist profile." : "Update the artist details."}</p>
        </div>
      </div>
      <form class="admin-form" method="post" action="${formAction}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="form-row">
          <label for="name">Name</label>
          <input id="name" type="text" name="name" value="<c:out value='${artist.name}'/>" required maxlength="255">
        </div>
        <div class="form-row">
          <label for="imageUrl">Image URL</label>
          <input id="imageUrl" type="url" name="imageUrl" value="<c:out value='${artist.imageUrl}'/>">
        </div>
        <div class="form-row">
          <label for="bio">Bio</label>
          <textarea id="bio" name="bio"><c:out value='${artist.bio}'/></textarea>
        </div>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit">
            <c:choose><c:when test="${isNew}">Create</c:when><c:otherwise>Save changes</c:otherwise></c:choose>
          </button>
          <a class="btn btn-secondary" href="/admin/artists">Cancel</a>
        </div>
      </form>

      <c:if test="${not empty org.springframework.validation.BindingResult.artist}">
        <div class="form-errors">
          <c:forEach items="${org.springframework.validation.BindingResult.artist.allErrors}" var="e">
            <div>${e.defaultMessage}</div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </main>
</body>
</html>
