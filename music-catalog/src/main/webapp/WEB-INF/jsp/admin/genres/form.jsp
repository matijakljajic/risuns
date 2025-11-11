<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html><body>
<c:set var="formAction" value="/admin/genres" />
<c:if test="${not empty genre.id}">
  <c:set var="formAction" value="${formAction}/${genre.id}" />
</c:if>
<h2><c:if test="${empty genre.id}">New</c:if><c:if test="${not empty genre.id}">Edit</c:if> Genre</h2>
<form method="post" action="${formAction}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <p>
    <label>Name</label><br>
    <input type="text" name="name" value="<c:out value='${genre.name}'/>" required maxlength="120">
  </p>
  <p><button type="submit">Save</button> <a href="/admin/genres">Cancel</a></p>
</form>

<c:if test="${not empty org.springframework.validation.BindingResult.genre}">
  <div style="color:red">
    <c:forEach items="${org.springframework.validation.BindingResult.genre.allErrors}" var="e">
      <div>${e.defaultMessage}</div>
    </c:forEach>
  </div>
</c:if>
</body></html>
