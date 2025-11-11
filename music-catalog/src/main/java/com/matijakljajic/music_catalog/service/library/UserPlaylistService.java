package com.matijakljajic.music_catalog.service.library;

import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.PlaylistItem;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.PlaylistItemRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserPlaylistService {

  private final PlaylistRepository playlists;
  private final PlaylistItemRepository playlistItems;
  private final TrackRepository tracks;
  private final UserRepository users;

  public ManageView buildManageView(Long ownerId, Authentication authentication) {
    User owner = users.findById(ownerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, owner)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    List<PlaylistRow> playlistRows = playlists.findByUserId(ownerId).stream()
        .sorted(Comparator.comparing(p -> p.getName().toLowerCase()))
        .map(p -> new PlaylistRow(p.getId(), p.getName(), p.isPublic()))
        .toList();
    boolean self = Objects.equals(viewer.getId(), owner.getId());
    return new ManageView(owner, playlistRows, self);
  }

  public List<PlaylistSummary> ownedPlaylists(Authentication authentication) {
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken
        || !authentication.isAuthenticated()) {
      return List.of();
    }
    User viewer = requireAuthenticated(authentication);
    return playlists.findByUserId(viewer.getId()).stream()
        .sorted(Comparator.comparing(p -> p.getName().toLowerCase()))
        .map(p -> new PlaylistSummary(p.getId(), p.getName(), p.isPublic()))
        .toList();
  }

  @Transactional
  public void createPlaylist(Long ownerId, String name, boolean isPublic, Authentication authentication) {
    User owner = users.findById(ownerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, owner)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    String trimmed = name != null ? name.trim() : "";
    if (trimmed.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
    }
    Playlist playlist = new Playlist();
    playlist.setUser(owner);
    playlist.setName(trimmed);
    playlist.setPublic(isPublic);
    playlist.setCreatedAt(Instant.now());
    playlists.save(playlist);
  }

  @Transactional
  public void updatePlaylist(Long ownerId, Long playlistId, String name, boolean isPublic,
                             Authentication authentication) {
    Playlist playlist = playlists.findById(playlistId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!Objects.equals(playlist.getUser().getId(), ownerId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, playlist.getUser())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    String trimmed = name != null ? name.trim() : "";
    if (trimmed.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
    }
    playlist.setName(trimmed);
    playlist.setPublic(isPublic);
    playlists.save(playlist);
  }

  @Transactional
  public void deletePlaylist(Long ownerId, Long playlistId, Authentication authentication) {
    Playlist playlist = playlists.findById(playlistId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!Objects.equals(playlist.getUser().getId(), ownerId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, playlist.getUser())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    playlists.delete(playlist);
  }

  @Transactional
  public void moveItem(Long playlistId, Long itemId, String direction, Authentication authentication) {
    Playlist playlist = playlists.findById(playlistId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, playlist.getUser())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    PlaylistItem item = playlistItems.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!Objects.equals(item.getPlaylist().getId(), playlistId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    int offset = parseDirection(direction);
    if (offset == 0) {
      return;
    }
    List<PlaylistItem> items = playlistItems.findByPlaylistIdOrderByPositionAsc(playlistId);
    items.sort(Comparator.comparingInt(PlaylistItem::getPosition));
    int index = -1;
    for (int i = 0; i < items.size(); i++) {
      if (Objects.equals(items.get(i).getId(), itemId)) {
        index = i;
        break;
      }
    }
    if (index < 0) {
      return;
    }
    int targetIndex = index + offset;
    if (targetIndex < 0 || targetIndex >= items.size()) {
      return;
    }
    PlaylistItem swap = items.get(targetIndex);
    items.set(targetIndex, items.get(index));
    items.set(index, swap);
    int size = items.size();
    for (int i = 0; i < size; i++) {
      items.get(i).setPosition(size + i + 1);
    }
    playlistItems.saveAllAndFlush(items);
    for (int i = 0; i < size; i++) {
      items.get(i).setPosition(i + 1);
    }
    playlistItems.saveAll(items);
  }

  @Transactional
  public void addTrackToPlaylist(Long targetPlaylistId, Long trackId, Authentication authentication) {
    Playlist playlist = playlists.findById(targetPlaylistId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    User viewer = requireAuthenticated(authentication);
    if (!canManage(viewer, playlist.getUser())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    Track track = tracks.findById(trackId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (playlistItems.existsByPlaylistIdAndTrackId(playlist.getId(), track.getId())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Track already in playlist");
    }
    Integer maxPosition = playlistItems.findMaxPosition(playlist.getId());
    PlaylistItem newItem = new PlaylistItem();
    newItem.setPlaylist(playlist);
    newItem.setTrack(track);
    newItem.setPosition((maxPosition == null ? 0 : maxPosition) + 1);
    playlistItems.save(newItem);
  }

  private User requireAuthenticated(Authentication authentication) {
    if (authentication == null
        || authentication instanceof AnonymousAuthenticationToken
        || !authentication.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return users.findByUsername(authentication.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
  }

  private boolean canManage(User viewer, User owner) {
    if (viewer == null || owner == null) {
      return false;
    }
    return viewer.isAdmin() || Objects.equals(viewer.getId(), owner.getId());
  }

  private int parseDirection(String direction) {
    if ("up".equalsIgnoreCase(direction)) {
      return -1;
    }
    if ("down".equalsIgnoreCase(direction)) {
      return 1;
    }
    return 0;
  }

  @Getter
  @AllArgsConstructor
  public static class ManageView {
    private final User owner;
    private final List<PlaylistRow> playlists;
    private final boolean self;
  }

  @Getter
  @AllArgsConstructor
  public static class PlaylistRow {
    private final Long id;
    private final String name;
    private final boolean isPublic;
  }

  @Getter
  @AllArgsConstructor
  public static class PlaylistSummary {
    private final Long id;
    private final String name;
    private final boolean isPublic;
  }
}
