package com.matijakljajic.music_catalog.service.search;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.TrackFeature;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

  @Mock
  private TrackRepository tracks;
  @Mock
  private ArtistRepository artists;
  @Mock
  private AlbumRepository albums;
  @Mock
  private PlaylistRepository playlists;
  @Mock
  private UserRepository users;
  @InjectMocks
  private SearchService service;

  @Test
  void searchTracks_mapsAlbumArtistGenresAndDedupedFeatures() {
    Artist primary = new Artist();
    primary.setId(1L);
    primary.setName("Primary");

    Album album = new Album();
    album.setId(2L);
    album.setTitle("Album");
    album.setPrimaryArtist(primary);

    Artist featureArtist = new Artist();
    featureArtist.setId(3L);
    featureArtist.setName("Feat");

    Genre rock = new Genre();
    rock.setId(4L);
    rock.setName("Rock");
    Genre funk = new Genre();
    funk.setId(5L);
    funk.setName("Funk");

    Track track = new Track();
    track.setId(9L);
    track.setTitle("Track");
    track.setAlbum(album);
    track.setGenres(new LinkedHashSet<>(Set.of(funk, rock)));
    track.setFeatures(List.of(
        TrackFeature.builder().track(track).artist(featureArtist).creditOrder(2).build(),
        TrackFeature.builder().track(track).artist(featureArtist).creditOrder(1).build()
    ));

    when(tracks.search("track", null, null)).thenReturn(List.of(track));

    var results = service.searchTracks("track", null, null);

    assertThat(results).hasSize(1);
    var dto = results.getFirst();
    assertThat(dto.getAlbumTitle()).isEqualTo("Album");
    assertThat(dto.getPrimaryArtist().getName()).isEqualTo("Primary");
    assertThat(dto.getFeaturedArtists()).extracting(SearchService.ArtistRef::getName)
        .containsExactly("Feat");
    assertThat(dto.getGenres()).extracting(SearchService.GenreRef::getName)
        .containsExactly("Funk", "Rock");
  }

  @Test
  void searchGeneral_returnsEmptyWhenQueryBlank() {
    var results = service.searchGeneral("  ");
    assertThat(results.isEmpty()).isTrue();
  }
}
