<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin · Track</title>
  <%@ include file="/WEB-INF/jsp/admin/_styles.jspf" %>
</head>
<body>
  <c:set var="pageTitle" value="Tracks"/>
  <c:set var="formAction" value="/admin/tracks"/>
  <c:set var="isNew" value="${empty track.id}"/>
  <c:if test="${not empty track.id}">
    <c:set var="formAction" value="${formAction}/${track.id}"/>
  </c:if>
  <%@ include file="/WEB-INF/jsp/admin/_header.jspf" %>
  <main class="admin-main">
    <div class="admin-card">
      <div class="admin-card__head">
        <div>
          <h2><c:choose><c:when test="${isNew}">Create track</c:when><c:otherwise>Edit track</c:otherwise></c:choose></h2>
          <p>${isNew ? "Add a new track to the catalog." : "Update the selected track."}</p>
        </div>
      </div>
      <form class="admin-form" method="post" action="${formAction}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="form-row">
          <label for="title">Title</label>
          <input id="title" type="text" name="title" value="<c:out value='${track.title}'/>" required maxlength="255">
        </div>
        <div class="form-row">
          <label for="trackNo">Track No</label>
          <input id="trackNo" type="number" name="trackNo" value="<c:out value='${track.trackNo}'/>" min="1">
        </div>
        <div class="form-row">
          <label><input type="checkbox" name="explicit" <c:if test="${track.explicit}">checked</c:if>> Explicit</label>
        </div>
        <div class="form-row">
          <label for="album">Album</label>
          <select id="album" name="album.id" required>
            <c:forEach items="${albums}" var="al">
              <option value="${al.id}" <c:if test="${track.album != null && track.album.id == al.id}">selected</c:if>>
                ${al.title} (${al.releaseYear})
              </option>
            </c:forEach>
          </select>
        </div>
        <div class="form-row">
          <label for="genres">Genres</label>
          <select id="genres" name="genreIds" multiple size="8">
            <c:forEach items="${genres}" var="g">
              <c:set var="selected" value="false"/>
              <c:forEach items="${track.genres}" var="tg">
                <c:if test="${tg.id == g.id}"><c:set var="selected" value="true"/></c:if>
              </c:forEach>
              <option value="${g.id}" <c:if test="${selected}">selected</c:if>>${g.name}</option>
            </c:forEach>
          </select>
          <div class="helper-text">Hold Ctrl/Cmd to select multiple genres.</div>
        </div>
        <div class="form-row">
          <label for="features">Featured Artists</label>
          <select id="features" name="featureArtistIds" multiple size="8">
            <c:forEach items="${artists}" var="artist">
              <c:set var="selected" value="false"/>
              <c:forEach items="${track.features}" var="feat">
                <c:if test="${feat.artist != null && feat.artist.id == artist.id}">
                  <c:set var="selected" value="true"/>
                </c:if>
              </c:forEach>
              <option value="${artist.id}" <c:if test="${selected}">selected</c:if>>${artist.name}</option>
            </c:forEach>
          </select>
          <div class="helper-text">Hold Ctrl/Cmd to select multiple artists; order follows selection.</div>
        </div>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit">
            <c:choose><c:when test="${isNew}">Create</c:when><c:otherwise>Save changes</c:otherwise></c:choose>
          </button>
          <a class="btn btn-secondary" href="/admin/tracks">Cancel</a>
        </div>
      </form>

      <c:if test="${not empty org.springframework.validation.BindingResult.track}">
        <div class="form-errors">
          <c:forEach items="${org.springframework.validation.BindingResult.track.allErrors}" var="e">
            <div>${e.defaultMessage}</div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </main>
</body>
</html>
