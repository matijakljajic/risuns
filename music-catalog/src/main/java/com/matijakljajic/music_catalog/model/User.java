package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users",
  indexes = {
    @Index(name="ix_users_username", columnList = "username", unique = true),
    @Index(name="ix_users_email",    columnList = "email",    unique = true)
  })
@Getter @Setter @NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 80, unique = true)
  private String username;

  @Column(name="password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Column(nullable = false, length = 255, unique = true)
  private String email;

  @Column(name="display_name", length = 255)
  private String displayName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Role role = Role.USER;

  @Column(nullable = false)
  private boolean enabled = true;

  public boolean isAdmin() { return role == Role.ADMIN; }
  public boolean isUser()  { return role == Role.USER;  }
}
