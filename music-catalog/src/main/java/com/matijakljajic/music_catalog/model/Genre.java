package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre",
  indexes = { @Index(name="uq_genre_name", columnList = "name", unique = true) })
@Getter @Setter @NoArgsConstructor
public class Genre {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120, unique = true)
  private String name;
}
