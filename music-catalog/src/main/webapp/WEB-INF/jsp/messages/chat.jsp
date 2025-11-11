<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Messages</title>
  <%@ include file="/WEB-INF/jsp/_public_styles.jspf" %>
  <style>
    .messages-layout {
      display: grid;
      grid-template-columns: 260px 1fr;
      gap: 18px;
      min-height: calc(100vh - 120px);
    }
    .thread-panel {
      border: 1px solid #e4e7ef;
      padding: 16px;
      background: #f8fafc;
      display: flex;
      flex-direction: column;
      gap: 12px;
      max-height: calc(100vh - 160px);
      overflow-y: auto;
    }
    .thread-list {
      list-style: none;
      padding: 0;
      margin: 0;
      display: grid;
      gap: 8px;
    }
    .thread-item a {
      display: block;
      padding: 8px 10px;
      border: 1px solid #e2e8f0;
      background: #fff;
    }
    .thread-item.active a {
      background: #111827;
      color: #fff;
    }
    .chat-panel {
      border: 1px solid #e4e7ef;
      padding: 20px;
      background: #fff;
      min-height: 480px;
      display: flex;
      flex-direction: column;
      gap: 16px;
      max-height: calc(100vh - 160px);
    }
    .chat-messages {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 12px;
      overflow-y: auto;
      padding-right: 6px;
    }
    .bubble {
      max-width: 70%;
      padding: 10px 14px;
      border: 1px solid #e2e8f0;
      display: inline-flex;
      flex-direction: column;
      gap: 6px;
    }
    .bubble.incoming {
      align-self: flex-start;
      background: #f8fafc;
    }
    .bubble.outgoing {
      align-self: flex-end;
      background: #111827;
      color: #fff;
      border-color: #111827;
    }
    .bubble small {
      font-size: 0.75rem;
      opacity: 0.8;
    }
    .chat-form {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .chat-heading {
      margin: 0;
      font-size: 1.3rem;
    }
    .chat-heading a {
      font-weight: bold;
    }
    .chat-form textarea {
      width: 100%;
      border: 1px solid #cbd5f5;
      padding: 8px;
      font-family: inherit;
      box-sizing: border-box;
    }
    .empty-state {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 12px;
      color: #475569;
    }
  </style>
</head>
<body>
  <div class="page-shell">
    <a class="back-link" href="/">← Home</a>
    <div class="messages-layout">
      <aside class="thread-panel">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <strong>Conversations</strong>
          <a class="btn small" href="/messages/new">New</a>
        </div>
        <c:choose>
          <c:when test="${empty partners}">
            <p>No conversations yet.</p>
          </c:when>
          <c:otherwise>
            <ul class="thread-list">
              <c:forEach items="${partners}" var="partner">
                <li class="thread-item <c:if test='${activePartner != null && activePartner.id == partner.id}'>active</c:if>">
                  <a href="/messages?userId=${partner.id}">
                    ${partner.label}
                  </a>
                </li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </aside>

      <section class="chat-panel">
        <c:if test="${not empty message}">
          <div class="flash">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
          <div class="flash" style="border-color:#b91c1c; background:#fef2f2; color:#991b1b;">
            ${error}
          </div>
        </c:if>

        <c:choose>
          <c:when test="${activePartner == null}">
            <div class="empty-state">
              <p>Select a conversation or start a new one.</p>
              <a class="btn small" href="/messages/new">Compose message</a>
            </div>
          </c:when>
          <c:otherwise>
            <div style="display:flex; justify-content:space-between; align-items:center;">
              <h2 class="chat-heading">Chat with <a href="/users/${activePartner.id}">${activePartner.label}</a></h2>
            </div>
            <div class="chat-messages"
                 id="chatMessages"
                 data-partner="${activePartner.id}"
                 data-next="${nextPage != null ? nextPage : ''}">
              <c:choose>
                <c:when test="${empty chatMessages}">
                  <div class="empty-state" style="border:1px dashed #cbd5f5; padding:30px;">
                    <p>No messages yet. Say hi!</p>
                  </div>
                </c:when>
                <c:otherwise>
                  <c:forEach items="${chatMessages}" var="msg">
                    <div class="bubble ${msg.outgoing ? 'outgoing' : 'incoming'}">
                      <div><c:out value="${msg.content}"/></div>
                      <small>${msg.sentAt}</small>
                    </div>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
            </div>
            <form class="chat-form" method="post" action="/messages/chat">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <input type="hidden" name="recipientId" value="${activePartner.id}"/>
              <textarea name="content" rows="3" placeholder="Write a message..." required maxlength="2000"></textarea>
              <button type="submit" class="btn">Send</button>
            </form>
          </c:otherwise>
        </c:choose>
      </section>
    </div>
  </div>
  <script>
    (function() {
      const container = document.getElementById('chatMessages');
      if (!container) return;
      const partnerId = container.dataset.partner;
      if (!partnerId) return;

      const scrollToBottom = () => {
        container.scrollTop = container.scrollHeight;
      };
      scrollToBottom();

      let loading = false;

      const prependMessages = (messages) => {
        if (!Array.isArray(messages) || messages.length === 0) {
          return;
        }
        const previousHeight = container.scrollHeight;
        messages.forEach(msg => {
          const bubble = document.createElement('div');
          bubble.className = 'bubble ' + (msg.outgoing ? 'outgoing' : 'incoming');
          const body = document.createElement('div');
          body.textContent = msg.content;
          bubble.appendChild(body);
          const meta = document.createElement('small');
          meta.textContent = msg.sentAt || '';
          bubble.appendChild(meta);
          container.insertBefore(bubble, container.firstChild);
        });
        const newHeight = container.scrollHeight;
        container.scrollTop = container.scrollTop + (newHeight - previousHeight);
      };

      container.addEventListener('scroll', function() {
        if (container.scrollTop > 40 || loading) {
          return;
        }
        const nextPage = container.dataset.next;
        if (!nextPage) {
          return;
        }
        loading = true;
        fetch(`/messages/thread?userId=${partnerId}&page=${nextPage}`)
          .then(resp => {
            if (!resp.ok) {
              throw new Error('Failed to load messages');
            }
            return resp.json();
          })
          .then(chunk => {
            if (!chunk) return;
            prependMessages(chunk.messages);
            container.dataset.next = chunk.nextPage != null ? chunk.nextPage : '';
          })
          .catch(() => {
            console.warn('Unable to load older messages.');
          })
          .finally(() => {
            loading = false;
          });
      });
    })();
  </script>
</body>
</html>
