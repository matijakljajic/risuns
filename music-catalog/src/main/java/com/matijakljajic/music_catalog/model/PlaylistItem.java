package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "playlist_item",
  uniqueConstraints = {
    @UniqueConstraint(name = "uq_playlist_track", columnNames = {"playlist_id","track_id"}),
    // keep ordering sane per playlist; make it unique if you want strictness:
    @UniqueConstraint(name = "uq_playlist_position", columnNames = {"playlist_id","position"})
  },
  indexes = {
    @Index(name = "ix_pi_playlist_pos", columnList = "playlist_id, position")
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlaylistItem {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id", nullable = false)
  private Playlist playlist;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "track_id", nullable = false)
  private Track track;

  @Column(name = "position", nullable = false)
  private Integer position;
}
