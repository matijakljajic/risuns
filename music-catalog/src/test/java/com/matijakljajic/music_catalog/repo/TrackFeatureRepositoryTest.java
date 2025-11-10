package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.TrackFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class TrackFeatureRepositoryTest {

  @Autowired TrackFeatureRepository features;
  @Autowired TrackRepository tracks;
  @Autowired AlbumRepository albums;

  @Test
  void feather_hasShing02ThenUyama_byCreditOrder() {
    Long albumId = albums.findByTitleIgnoreCase("Modal Soul").orElseThrow().getId();
    Long trackId = tracks.findByAlbumIdOrderByTrackNoAsc(albumId).getFirst().getId(); // Feather (track_no=1)

    List<TrackFeature> list = features.findByTrackIdOrderByCreditOrderAsc(trackId);
    assertThat(list).hasSize(2);
    assertThat(list.get(0).getCreditOrder()).isEqualTo(1);
    assertThat(list.get(0).getArtist().getName()).isEqualTo("Shing02");
    assertThat(list.get(1).getCreditOrder()).isEqualTo(2);
    assertThat(list.get(1).getArtist().getName()).isEqualTo("Uyama Hiroto");
  }
}
