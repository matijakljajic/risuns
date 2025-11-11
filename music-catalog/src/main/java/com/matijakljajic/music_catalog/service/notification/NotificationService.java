package com.matijakljajic.music_catalog.service.notification;

import com.matijakljajic.music_catalog.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final ObjectProvider<JavaMailSender> mailSenderProvider;

  @Value("${app.notifications.from:noreply@localhost}")
  private String fromAddress;

  public void notifyNewMessage(User recipient, User sender, String content) {
    if (recipient == null || sender == null) {
      return;
    }
    JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
    if (mailSender == null) {
      log.debug("Mail sender not configured; skipping email notification.");
      return;
    }
    if (recipient.getEmail() == null || recipient.getEmail().isBlank()) {
      return;
    }
    String snippet = content != null ? content.trim() : "";
    if (snippet.length() > 200) {
      snippet = snippet.substring(0, 200) + "…";
    }
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(recipient.getEmail());
    message.setFrom(fromAddress);
    message.setSubject("New message from " + sender.getUsername());
    message.setText("""
        You got a new message from %s:

        %s

        Sign in to reply.
        """.formatted(defaultDisplay(sender), snippet));
    try {
      mailSender.send(message);
    } catch (MailException ex) {
      log.warn("Failed to send message notification to {}", recipient.getEmail(), ex);
    }
  }

  private String defaultDisplay(User sender) {
    if (sender.getDisplayName() != null && !sender.getDisplayName().isBlank()) {
      return sender.getDisplayName();
    }
    return sender.getUsername();
  }
}
