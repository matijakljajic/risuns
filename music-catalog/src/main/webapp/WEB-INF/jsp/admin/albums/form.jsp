<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Album</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Albums"/>
  <c:set var="formAction" value="/admin/albums"/>
  <c:set var="isNew" value="${empty album.id}"/>
  <c:if test="${not empty album.id}">
    <c:set var="formAction" value="${formAction}/${album.id}"/>
  </c:if>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2><c:choose><c:when test="${isNew}">Create album</c:when><c:otherwise>Edit album</c:otherwise></c:choose></h2>
          <p>${isNew ? "Add a new album release." : "Update album details."}</p>
        </div>
      </div>
      <form class="admin-form" method="post" action="${formAction}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="form-row">
          <label for="title">Title</label>
          <input id="title" type="text" name="title" value="<c:out value='${album.title}'/>" required maxlength="255">
        </div>
        <div class="form-row">
          <label for="releaseYear">Release Year</label>
          <input id="releaseYear" type="number" name="releaseYear" value="<c:out value='${album.releaseYear}'/>" min="1900" max="2100" required>
        </div>
        <div class="form-row">
          <label for="primaryArtist">Primary Artist</label>
          <select id="primaryArtist" name="primaryArtist.id" required>
            <c:forEach items="${artists}" var="a">
              <option value="${a.id}" <c:if test="${album.primaryArtist != null && album.primaryArtist.id == a.id}">selected</c:if>>${a.name}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-row">
          <label for="coverUrl">Cover URL</label>
          <input id="coverUrl" type="url" name="coverUrl" value="<c:out value='${album.coverUrl}'/>">
        </div>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit">
            <c:choose><c:when test="${isNew}">Create</c:when><c:otherwise>Save changes</c:otherwise></c:choose>
          </button>
          <a class="btn btn-secondary" href="/admin/albums">Cancel</a>
        </div>
      </form>

      <c:if test="${not empty org.springframework.validation.BindingResult.album}">
        <div class="form-errors">
          <c:forEach items="${org.springframework.validation.BindingResult.album.allErrors}" var="e">
            <div>${e.defaultMessage}</div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </main>
</body>
</html>
