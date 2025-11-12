package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "track",
  uniqueConstraints = @UniqueConstraint(name="uq_track_trackno", columnNames = {"album_id","track_no"}),
  indexes = {
    @Index(name="ix_track_album", columnList = "album_id"),
    @Index(name="ix_track_title", columnList = "title")
  })
@Getter @Setter @NoArgsConstructor
public class Track {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) @JoinColumn(name="album_id")
  private Album album;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name="track_no")
  private Integer trackNo;

  @Column(name="is_explicit", nullable = false)
  private boolean explicit;

  @ManyToMany
  @JoinTable(
    name = "track_genre",
    joinColumns = @JoinColumn(name = "track_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<Genre> genres = new LinkedHashSet<>();

  @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("creditOrder ASC")
  private List<TrackFeature> features = new ArrayList<>();

  public String displayArtist() {
    String primary = album.getPrimaryArtist().getName();
    String feat = features.stream()
        .sorted(java.util.Comparator.comparing(TrackFeature::getCreditOrder))
        .map(tf -> tf.getArtist().getName())
        .reduce((a,b) -> a + ", " + b).orElse("");
    return feat.isEmpty() ? primary : primary + " feat. " + feat;
  }
}
