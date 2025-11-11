package com.matijakljajic.music_catalog.service.listening;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.ListenRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListeningStatsService {

  private static final int DEFAULT_LIMIT = 5;

  private final ListenRepository listens;

  public List<TopListener> topListenersForAlbum(Long albumId) {
    return topListenersForAlbum(albumId, DEFAULT_LIMIT);
  }

  public List<TopListener> topListenersForAlbum(Long albumId, int limit) {
    return listens.topListenersForAlbum(albumId, PageRequest.of(0, limit))
        .stream()
        .map(this::mapRow)
        .toList();
  }

  public List<TopListener> topListenersForPlaylist(Long playlistId) {
    return topListenersForPlaylist(playlistId, DEFAULT_LIMIT);
  }

  public List<TopListener> topListenersForPlaylist(Long playlistId, int limit) {
    return listens.topListenersForPlaylist(playlistId, PageRequest.of(0, limit))
        .stream()
        .map(this::mapRow)
        .toList();
  }

  private TopListener mapRow(Object[] row) {
    User user = (User) row[0];
    Long plays = (Long) row[1];
    return new TopListener(
        user.getId(),
        user.getDisplayName(),
        user.getUsername(),
        plays != null ? plays : 0L
    );
  }

  @Getter
  @AllArgsConstructor
  public static class TopListener {
    private final Long userId;
    private final String displayName;
    private final String username;
    private final Long plays;

    public String getLabel() {
      return displayName != null && !displayName.isBlank() ? displayName : username;
    }
  }
}
