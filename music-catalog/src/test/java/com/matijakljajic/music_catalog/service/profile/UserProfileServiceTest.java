package com.matijakljajic.music_catalog.service.profile;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Listen;
import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.ListenRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

  @Mock
  private UserRepository users;
  @Mock
  private PlaylistRepository playlists;
  @Mock
  private ListenRepository listens;
  @InjectMocks
  private UserProfileService service;

  @Test
  void buildProfile_selfSeesPrivatePlaylistsAndStats() {
    User user = user(1L, "alice");
    when(users.findById(1L)).thenReturn(Optional.of(user));

    Playlist pub = playlist(10L, true, user);
    Playlist priv = playlist(11L, false, user);
    when(playlists.findByUserId(1L)).thenReturn(List.of(pub, priv));

    var listenData = List.of(
        listen(user, track(100L, "Song A", 200L, "Album A")),
        listen(user, track(101L, "Song B", 201L, "Album B")),
        listen(user, track(100L, "Song A", 200L, "Album A"))
    );
    when(listens.findByUserIdAndListenedAtGreaterThanEqualOrderByListenedAtDesc(eq(1L), any(Instant.class)))
        .thenReturn(listenData);

    Authentication auth = authentication("alice");

    var view = service.buildProfile(1L, auth, 0);

    assertThat(view.isSelf()).isTrue();
    assertThat(view.getVisiblePlaylists()).containsExactly(pub, priv);
    assertThat(view.getPrivatePlaylistCount()).isEqualTo(1);
    assertThat(view.getWeeklyListenCount()).isEqualTo(3);
    assertThat(view.getWeeklyUniqueTracks()).isEqualTo(2);
    assertThat(view.getTopTracks())
        .extracting(UserProfileService.TopTrackStat::getTrackTitle)
        .containsExactly("Song A", "Song B");
  }

  @Test
  void buildProfile_nonOwnerSeesOnlyPublicPlaylists() {
    User user = user(2L, "bob");
    when(users.findById(2L)).thenReturn(Optional.of(user));

    Playlist pub = playlist(20L, true, user);
    Playlist priv = playlist(21L, false, user);
    when(playlists.findByUserId(2L)).thenReturn(List.of(pub, priv));
    when(listens.findByUserIdAndListenedAtGreaterThanEqualOrderByListenedAtDesc(eq(2L), any(Instant.class)))
        .thenReturn(List.of());

    Authentication stranger = authentication("someone-else");

    var view = service.buildProfile(2L, stranger, 0);

    assertThat(view.isSelf()).isFalse();
    assertThat(view.getVisiblePlaylists()).containsExactly(pub);
    assertThat(view.getPrivatePlaylistCount()).isEqualTo(1);
  }

  private static User user(Long id, String username) {
    User user = new User();
    user.setId(id);
    user.setUsername(username);
    return user;
  }

  private static Playlist playlist(Long id, boolean isPublic, User owner) {
    Playlist playlist = new Playlist();
    playlist.setId(id);
    playlist.setPublic(isPublic);
    playlist.setUser(owner);
    return playlist;
  }

  private static Listen listen(User user, Track track) {
    Listen listen = new Listen();
    listen.setUser(user);
    listen.setTrack(track);
    listen.setListenedAt(Instant.now());
    return listen;
  }

  private static Track track(Long id, String title, Long albumId, String albumTitle) {
    Album album = new Album();
    album.setId(albumId);
    album.setTitle(albumTitle);
    album.setPrimaryArtist(new com.matijakljajic.music_catalog.model.Artist());

    Track track = new Track();
    track.setId(id);
    track.setTitle(title);
    track.setAlbum(album);
    track.setFeatures(new ArrayList<>());
    return track;
  }

  private static Authentication authentication(String username) {
    Authentication auth = mock(Authentication.class);
    when(auth.isAuthenticated()).thenReturn(true);
    when(auth.getName()).thenReturn(username);
    return auth;
  }
}
