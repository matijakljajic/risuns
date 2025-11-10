package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "listen",
  indexes = { @Index(name="ix_listen_user_time", columnList = "user_id, listened_at") })
@Getter @Setter @NoArgsConstructor
public class Listen {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) @JoinColumn(name="user_id")
  private User user;

  @ManyToOne(optional = false) @JoinColumn(name="track_id")
  private Track track;

  @Column(name="listened_at", nullable = false)
  private Instant listenedAt = Instant.now();

  public static Listen of(User u, Track t, java.time.Instant when) {
    var l = new Listen();
    l.setUser(u);
    l.setTrack(t);
    l.setListenedAt(when != null ? when : java.time.Instant.now());
    return l;
  }
}
