package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "album",
  uniqueConstraints = @UniqueConstraint(
    name="uq_album",
    columnNames = {"primary_artist_id","title","release_year"}
  ),
  indexes = {
    @Index(name="ix_album_title",  columnList = "title"),
    @Index(name="ix_album_artist", columnList = "primary_artist_id"),
    @Index(name="ix_album_year",   columnList = "release_year")
  })
@Getter @Setter @NoArgsConstructor
public class Album {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "primary_artist_id", nullable = false)
  private Artist primaryArtist;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name = "release_year")
  private Integer releaseYear;

  @Column(name="cover_url", length = 512)
  private String coverUrl;
}

