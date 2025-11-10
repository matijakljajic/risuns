package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artist",
  indexes = { @Index(name="uq_artist_name", columnList = "name", unique = true) })
@Getter @Setter @NoArgsConstructor
public class Artist {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(columnDefinition = "text")
  private String bio;

  @Column(name="image_url", length = 512)
  private String imageUrl;
}
