package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "message",
  indexes = {
    @Index(name="ix_message_inbox", columnList = "receiver_id, sent_at"),
    @Index(name="ix_message_outbox", columnList = "sender_id, sent_at")
  })
@Getter @Setter @NoArgsConstructor
public class Message {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) @JoinColumn(name="sender_id")
  private User sender;

  @ManyToOne(optional = false) @JoinColumn(name="receiver_id")
  private User receiver;

  @Column(nullable = false, length = 2000)
  private String content;

  @Column(name="sent_at", nullable = false)
  private Instant sentAt = Instant.now();

  public static Message of(User sender, User receiver, String content) {
    var m = new Message();
    m.setSender(sender);
    m.setReceiver(receiver);
    m.setContent(content);
    m.setSentAt(java.time.Instant.now());
    return m;
  }
}
