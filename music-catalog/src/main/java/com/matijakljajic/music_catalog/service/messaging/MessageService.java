package com.matijakljajic.music_catalog.service.messaging;

import com.matijakljajic.music_catalog.model.Message;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.MessageRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import com.matijakljajic.music_catalog.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {

  private static final DateTimeFormatter SENT_AT_FORMAT =
      DateTimeFormatter.ofPattern("d MMM uuuu · HH:mm").withZone(ZoneId.systemDefault());
  private static final int CHAT_PAGE_SIZE = 20;
  private final MessageRepository messages;
  private final UserRepository users;
  private final NotificationService notifications;

  public ConversationView loadConversation(Authentication authentication, Long requestedUserId) {
    User current = requireUser(authentication);
    List<RecipientOption> partners = conversationPartners(current, requestedUserId);
    RecipientOption selected = selectCounterpart(current, partners, requestedUserId);
    ConversationChunk chunk = selected == null
        ? new ConversationChunk(List.of(), null)
        : fetchConversationChunk(current, selected.getId(), 0);
    return new ConversationView(current, partners, selected, chunk.getMessages(), chunk.getNextPage());
  }

  public ConversationChunk loadMore(Authentication authentication, Long partnerId, int page) {
    User current = requireUser(authentication);
    if (partnerId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
    }
    if (Objects.equals(current.getId(), partnerId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot chat with yourself");
    }
    users.findById(partnerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return fetchConversationChunk(current, partnerId, page);
  }

  public List<RecipientOption> recipientOptions(Authentication authentication) {
    User current = requireUser(authentication);
    return allOtherUsers(current);
  }

  public ComposeContext prepareCompose(Authentication authentication, Long preselectId) {
    User current = requireUser(authentication);
    List<RecipientOption> options = allOtherUsers(current);
    RecipientOption preselected = null;
    if (preselectId != null) {
      preselected = options.stream()
          .filter(opt -> opt.getId().equals(preselectId))
          .findFirst()
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    return new ComposeContext(current, options, preselected);
  }

  @Transactional
  public void sendMessage(Authentication authentication, Long recipientId, String content) {
    User sender = requireUser(authentication);
    if (recipientId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient is required");
    }
    User recipient = users.findById(recipientId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if (Objects.equals(sender.getId(), recipient.getId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot message yourself");
    }
    String body = content != null ? content.trim() : "";
    if (body.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message cannot be empty");
    }
    if (body.length() > 2000) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message is too long");
    }
    Message persisted = messages.save(Message.of(sender, recipient, body));
    if (recipient.isNotifyOnMessage()) {
      notifications.notifyNewMessage(recipient, sender, body);
    }
  }

  private User requireUser(Authentication authentication) {
    if (authentication == null
        || authentication instanceof AnonymousAuthenticationToken
        || !authentication.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return users.findByUsername(authentication.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
  }

  private List<RecipientOption> allOtherUsers(User current) {
    return users.findAll(Sort.by(Sort.Direction.ASC, "username")).stream()
        .filter(u -> !Objects.equals(u.getId(), current.getId()))
        .map(RecipientOption::from)
        .toList();
  }

  private List<RecipientOption> conversationPartners(User current, Long requestedUserId) {
    List<User> incoming = messages.findDistinctSendersByReceiverId(current.getId());
    List<User> outgoing = messages.findDistinctReceiversBySenderId(current.getId());
    LinkedHashMap<Long, RecipientOption> dedup = new LinkedHashMap<>();
    incoming.stream()
        .filter(Objects::nonNull)
        .filter(u -> !Objects.equals(u.getId(), current.getId()))
        .forEach(u -> dedup.putIfAbsent(u.getId(), RecipientOption.from(u)));
    outgoing.stream()
        .filter(Objects::nonNull)
        .filter(u -> !Objects.equals(u.getId(), current.getId()))
        .forEach(u -> dedup.putIfAbsent(u.getId(), RecipientOption.from(u)));
    List<RecipientOption> partners = new ArrayList<>(dedup.values());
    partners.sort(Comparator.comparing(RecipientOption::getLabel, String.CASE_INSENSITIVE_ORDER));
    return partners;
  }

  private RecipientOption selectCounterpart(User current,
                                            List<RecipientOption> partners,
                                            Long requestedUserId) {
    if (requestedUserId != null) {
      if (Objects.equals(requestedUserId, current.getId())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot open conversation with yourself");
      }
      RecipientOption existing = partners.stream()
          .filter(opt -> opt.getId().equals(requestedUserId))
          .findFirst()
          .orElse(null);
      if (existing != null) {
        return existing;
      }
      User user = users.findById(requestedUserId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
      RecipientOption extra = RecipientOption.from(user);
      partners.add(extra);
      partners.sort(Comparator.comparing(RecipientOption::getLabel, String.CASE_INSENSITIVE_ORDER));
      return extra;
    }
    return partners.isEmpty() ? null : partners.get(0);
  }

  @Getter
  @AllArgsConstructor
  public static class ComposeContext {
    private final User currentUser;
    private final List<RecipientOption> recipientOptions;
    private final RecipientOption preselected;
  }

  @Getter
  @AllArgsConstructor
  public static class RecipientOption {
    private final Long id;
    private final String username;
    private final String label;

    static RecipientOption from(User user) {
      String label = user.getDisplayName() != null && !user.getDisplayName().isBlank()
          ? user.getDisplayName() + " (" + user.getUsername() + ")"
          : user.getUsername();
      return new RecipientOption(user.getId(), user.getUsername(), label);
    }
  }

  @Getter
  @AllArgsConstructor
  public static class ConversationView {
    private final User currentUser;
    private final List<RecipientOption> partners;
    private final RecipientOption activePartner;
    private final List<ChatMessage> messages;
    private final Integer nextPage;
  }

  @Getter
  @AllArgsConstructor
  public static class ChatMessage {
    private final Long id;
    private final boolean outgoing;
    private final String content;
    private final String sentAt;

    static ChatMessage from(Message message, Long currentUserId) {
      boolean outgoing = message.getSender() != null
          && Objects.equals(message.getSender().getId(), currentUserId);
      String formatted = message.getSentAt() != null ? SENT_AT_FORMAT.format(message.getSentAt()) : "";
      return new ChatMessage(message.getId(), outgoing, message.getContent(), formatted);
    }
  }

  @Getter
  @AllArgsConstructor
  public static class ConversationChunk {
    private final List<ChatMessage> messages;
    private final Integer nextPage;
  }

  private ConversationChunk fetchConversationChunk(User current, Long partnerId, int page) {
    int safePage = Math.max(page, 0);
    Pageable pageable = PageRequest.of(safePage, CHAT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "sentAt"));
    Page<Message> result = messages.findConversationPage(current.getId(), partnerId, pageable);
    List<Message> content = new ArrayList<>(result.getContent());
    Collections.reverse(content);
    List<ChatMessage> chatMessages = content.stream()
        .map(m -> ChatMessage.from(m, current.getId()))
        .toList();
    Integer nextPage = result.hasNext() ? safePage + 1 : null;
    return new ConversationChunk(chatMessages, nextPage);
  }
}
