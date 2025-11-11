package com.matijakljajic.music_catalog.service.listening;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.model.Listen;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.TrackFeature;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.ListenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecentListenServiceTest {

  @Mock
  private ListenRepository listens;
  @InjectMocks
  private RecentListenService service;

  @Test
  void latest_projectsSortedFeaturedArtistsAndPrimaryInfo() {
    Artist primary = new Artist();
    primary.setId(1L);
    primary.setName("Primary");

    Album album = new Album();
    album.setId(2L);
    album.setTitle("Album");
    album.setPrimaryArtist(primary);

    Track track = new Track();
    track.setId(3L);
    track.setTitle("Song");
    track.setAlbum(album);

    Artist featA = new Artist();
    featA.setId(11L);
    featA.setName("Feat A");
    Artist featB = new Artist();
    featB.setId(12L);
    featB.setName("Feat B");

    TrackFeature featureB = TrackFeature.builder()
        .track(track)
        .artist(featB)
        .creditOrder(2)
        .build();
    TrackFeature featureA = TrackFeature.builder()
        .track(track)
        .artist(featA)
        .creditOrder(1)
        .build();
    track.setFeatures(new ArrayList<>(List.of(featureB, featureA)));

    User user = new User();
    user.setId(9L);
    user.setUsername("listener");

    Listen listen = new Listen();
    listen.setId(100L);
    listen.setTrack(track);
    listen.setUser(user);
    listen.setListenedAt(Instant.parse("2024-01-01T10:00:00Z"));

    when(listens.findAllByOrderByListenedAtDesc(PageRequest.of(0, 5))).thenReturn(List.of(listen));

    var results = service.latest(5);

    assertThat(results).hasSize(1);
    var view = results.getFirst();
    assertThat(view.getTrackTitle()).isEqualTo("Song");
    assertThat(view.getPrimaryArtist().getName()).isEqualTo("Primary");
    assertThat(view.getFeaturedArtists())
        .extracting(RecentListenService.ArtistRef::getName)
        .containsExactly("Feat A", "Feat B");
  }
}
