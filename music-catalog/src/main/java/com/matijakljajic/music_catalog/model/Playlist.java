package com.matijakljajic.music_catalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "playlist",
  uniqueConstraints = @UniqueConstraint(name="uq_playlist_name", columnNames = {"user_id","name"}),
  indexes = { @Index(name="ix_playlist_user", columnList = "user_id") })
@Getter @Setter @NoArgsConstructor
public class Playlist {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) @JoinColumn(name="user_id")
  private User user;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name="is_public", nullable = false)
  private boolean isPublic = false;

  @Column(name="created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("position ASC")
  private List<PlaylistItem> items = new ArrayList<>();

  public PlaylistItem addTrackAt(Track track, int position) {
  if (items.stream().anyMatch(it -> it.getTrack().getId() != null && it.getTrack().getId().equals(track.getId()))) {
    throw new IllegalArgumentException("Track already in playlist");
  }
  var it = new PlaylistItem();
  it.setPlaylist(this);
  it.setTrack(track);
  it.setPosition(position);
  items.add(it);
  items.sort(java.util.Comparator.comparingInt(PlaylistItem::getPosition));
  for (int i = 0; i < items.size(); i++) items.get(i).setPosition(i + 1);
  return it;
  }

  public PlaylistItem addTrackToEnd(Track track) {
    int next = items.isEmpty() ? 1 : items.get(items.size() - 1).getPosition() + 1;
    return addTrackAt(track, next);
  }

  public void move(int fromPosition, int toPosition) {
    items.sort(java.util.Comparator.comparingInt(PlaylistItem::getPosition));
    var it = items.remove(fromPosition - 1);
    items.add(Math.max(0, Math.min(toPosition - 1, items.size())), it);
    for (int i = 0; i < items.size(); i++) items.get(i).setPosition(i + 1);
  }
}