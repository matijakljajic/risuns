package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService.TopListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumViewServiceTest {

  @Mock
  private AlbumRepository albums;
  @Mock
  private TrackRepository tracks;
  @Mock
  private ListeningStatsService listeningStats;
  @InjectMocks
  private AlbumViewService service;

  @Test
  void getAlbum_fetchesAlbumTracksAndTopListeners() {
    var artist = new Artist();
    artist.setId(7L);
    artist.setName("Main Artist");

    var album = new Album();
    album.setId(42L);
    album.setTitle("Test Album");
    album.setPrimaryArtist(artist);

    Track track = new Track();
    track.setId(101L);
    track.setAlbum(album);
    track.setTitle("Intro");

    List<TopListener> listeners = List.of(
        new TopListener(1L, "Alice", "alice", 5L),
        new TopListener(2L, null, "bob", 3L)
    );

    when(albums.findById(42L)).thenReturn(Optional.of(album));
    when(tracks.findByAlbumIdOrderByTrackNoAsc(42L)).thenReturn(List.of(track));
    when(listeningStats.topListenersForAlbum(42L)).thenReturn(listeners);

    var view = service.getAlbum(42L);

    assertThat(view.getAlbum()).isSameAs(album);
    assertThat(view.getTracks()).containsExactly(track);
    assertThat(view.getTopListeners()).isEqualTo(listeners);
  }
}
