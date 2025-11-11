package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.PlaylistItem;
import com.matijakljajic.music_catalog.repository.PlaylistItemRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistViewService {

  private final PlaylistRepository playlists;
  private final PlaylistItemRepository playlistItems;
  private final ListeningStatsService listeningStats;

  public PlaylistView getPlaylist(Long playlistId, Authentication authentication) {
    Playlist playlist = playlists.findById(playlistId).orElseThrow();
    if (!canView(playlist, authentication)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found");
    }
    List<PlaylistItem> items = playlistItems.findByPlaylistIdOrderByPositionAsc(playlistId);
    var topListeners = listeningStats.topListenersForPlaylist(playlistId);
    return new PlaylistView(playlist, items, topListeners);
  }

  private boolean canView(Playlist playlist, Authentication authentication) {
    if (playlist.isPublic()) {
      return true;
    }
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }
    String username = authentication.getName();
    boolean isOwner = playlist.getUser() != null
        && playlist.getUser().getUsername() != null
        && playlist.getUser().getUsername().equalsIgnoreCase(username);
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
    return isOwner || isAdmin;
  }

  @Getter
  @AllArgsConstructor
  public static class PlaylistView {
    private final Playlist playlist;
    private final List<PlaylistItem> items;
    private final List<ListeningStatsService.TopListener> topListeners;
  }
}
