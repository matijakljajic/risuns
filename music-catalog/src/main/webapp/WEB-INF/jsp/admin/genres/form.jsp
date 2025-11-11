<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Genre</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Genres"/>
  <c:set var="formAction" value="/admin/genres"/>
  <c:set var="isNew" value="${empty genre.id}"/>
  <c:if test="${not empty genre.id}">
    <c:set var="formAction" value="${formAction}/${genre.id}"/>
  </c:if>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2><c:choose><c:when test="${isNew}">Create genre</c:when><c:otherwise>Edit genre</c:otherwise></c:choose></h2>
          <p>${isNew ? "Add a new genre to the catalog." : "Update the selected genre."}</p>
        </div>
      </div>
      <form class="admin-form" method="post" action="${formAction}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="form-row">
          <label for="name">Name</label>
          <input id="name" type="text" name="name" value="<c:out value='${genre.name}'/>" required maxlength="120">
        </div>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit">
            <c:choose><c:when test="${isNew}">Create</c:when><c:otherwise>Save changes</c:otherwise></c:choose>
          </button>
          <a class="btn btn-secondary" href="/admin/genres">Cancel</a>
        </div>
      </form>

      <c:if test="${not empty org.springframework.validation.BindingResult.genre}">
        <div class="form-errors">
          <c:forEach items="${org.springframework.validation.BindingResult.genre.allErrors}" var="e">
            <div>${e.defaultMessage}</div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </main>
</body>
</html>
