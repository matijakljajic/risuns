package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.PlaylistItem;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.PlaylistItemRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService.TopListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistViewServiceTest {

  @Mock
  private PlaylistRepository playlists;
  @Mock
  private PlaylistItemRepository playlistItems;
  @Mock
  private ListeningStatsService listeningStats;
  @InjectMocks
  private PlaylistViewService service;

  @Test
  void publicPlaylist_isVisibleWithoutAuthentication() {
    Playlist playlist = playlistWithOwner(true, "owner");

    PlaylistItem item = new PlaylistItem();
    item.setTrack(new Track());

    when(playlists.findById(99L)).thenReturn(Optional.of(playlist));
    when(playlistItems.findByPlaylistIdOrderByPositionAsc(99L)).thenReturn(List.of(item));
    when(listeningStats.topListenersForPlaylist(99L)).thenReturn(List.of(
        new TopListener(1L, "Alice", "alice", 3L)
    ));

    var view = service.getPlaylist(99L, null);

    assertThat(view.getPlaylist()).isSameAs(playlist);
    assertThat(view.getItems()).containsExactly(item);
    assertThat(view.getTopListeners()).hasSize(1);
  }

  @Test
  void privatePlaylist_visibleToOwner() {
    Playlist playlist = playlistWithOwner(false, "owner");

    when(playlists.findById(99L)).thenReturn(Optional.of(playlist));
    when(playlistItems.findByPlaylistIdOrderByPositionAsc(99L)).thenReturn(List.of());
    when(listeningStats.topListenersForPlaylist(99L)).thenReturn(List.of());

    Authentication auth = authenticatedUser("owner");
    var view = service.getPlaylist(99L, auth);

    assertThat(view.getPlaylist()).isSameAs(playlist);
  }

  @Test
  void privatePlaylist_hiddenFromOtherUsers() {
    Playlist playlist = playlistWithOwner(false, "owner");

    when(playlists.findById(99L)).thenReturn(Optional.of(playlist));

    Authentication auth = authenticatedUser("intruder");

    assertThrows(ResponseStatusException.class, () -> service.getPlaylist(99L, auth));
  }

  private static Playlist playlistWithOwner(boolean isPublic, String username) {
    User user = new User();
    user.setUsername(username);

    Playlist playlist = new Playlist();
    playlist.setId(99L);
    playlist.setPublic(isPublic);
    playlist.setUser(user);
    return playlist;
  }

  private static Authentication authenticatedUser(String username) {
    Authentication auth = mock(Authentication.class);
    when(auth.isAuthenticated()).thenReturn(true);
    when(auth.getName()).thenReturn(username);
    Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    when(auth.getAuthorities()).thenReturn((Collection) authorities);
    return auth;
  }
}
