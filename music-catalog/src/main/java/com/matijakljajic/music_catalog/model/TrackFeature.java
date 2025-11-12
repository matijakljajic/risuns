package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "track_feature",
  uniqueConstraints = {
    @UniqueConstraint(name = "uq_track_artist", columnNames = {"track_id", "artist_id"})
  },
  indexes = {
    @Index(name = "ix_tf_track",        columnList = "track_id"),
    @Index(name = "ix_tf_artist",       columnList = "artist_id"),
    @Index(name = "ix_tf_track_order",  columnList = "track_id, credit_order")
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrackFeature {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "track_id", nullable = false)
  private Track track;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "artist_id", nullable = false)
  private Artist artist;

  @Column(name = "credit_order")
  private Integer creditOrder;
}
