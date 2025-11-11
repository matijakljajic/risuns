<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><body>
<c:set var="formAction" value="/admin/tracks" />
<c:if test="${not empty track.id}">
  <c:set var="formAction" value="${formAction}/${track.id}" />
</c:if>
<h2><c:if test="${empty track.id}">New</c:if><c:if test="${not empty track.id}">Edit</c:if> Track</h2>
<form method="post" action="${formAction}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <p>
    <label>Title</label><br>
    <input type="text" name="title" value="<c:out value='${track.title}'/>" required maxlength="255">
  </p>
  <p>
    <label>Track No</label><br>
    <input type="number" name="trackNo" value="<c:out value='${track.trackNo}'/>" min="1">
  </p>
  <p>
    <label>Explicit</label>
    <input type="checkbox" name="explicit" <c:if test="${track.explicit}">checked</c:if> >
  </p>
  <p>
    <label>Album</label><br>
    <select name="album.id" required>
      <c:forEach items="${albums}" var="al">
        <option value="${al.id}" <c:if test="${track.album != null && track.album.id == al.id}">selected</c:if>>
          ${al.title} (${al.releaseYear})
        </option>
      </c:forEach>
    </select>
  </p>
  <p>
    <label>Genres</label><br>
    <select name="genreIds" multiple size="8">
      <c:forEach items="${genres}" var="g">
        <c:set var="selected" value="false"/>
        <c:forEach items="${track.genres}" var="tg">
          <c:if test="${tg.id == g.id}"><c:set var="selected" value="true"/></c:if>
        </c:forEach>
        <option value="${g.id}" <c:if test="${selected}">selected</c:if>>${g.name}</option>
      </c:forEach>
    </select>
  </p>
  <p><button type="submit">Save</button> <a href="/admin/tracks">Cancel</a></p>
</form>

<c:if test="${not empty org.springframework.validation.BindingResult.track}">
  <div style="color:red">
    <c:forEach items="${org.springframework.validation.BindingResult.track.allErrors}" var="e">
      <div>${e.defaultMessage}</div>
    </c:forEach>
  </div>
</c:if>
</body></html>
