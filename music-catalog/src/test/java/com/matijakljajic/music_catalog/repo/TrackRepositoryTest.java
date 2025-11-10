package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Track;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class TrackRepositoryTest {

  @Autowired TrackRepository tracks;
  @Autowired AlbumRepository albums;

  @Test
  void findByTitleContainingIgnoreCase_feath_containsFeather() {
    var list = tracks.findByTitleContainingIgnoreCase("feath");
    assertThat(list).extracting(Track::getTitle).contains("Feather");
  }

  @Test
  void findByAlbumIdOrderByTrackNoAsc_modalSoul_sortedFeatherThenLuvSic3() {
    Long albumId = albums.findByTitleIgnoreCase("Modal Soul").orElseThrow().getId();
    var list = tracks.findByAlbumIdOrderByTrackNoAsc(albumId);
    assertThat(list).hasSize(2);
    assertThat(list.get(0).getTrackNo()).isEqualTo(1);
    assertThat(list.get(0).getTitle()).isEqualTo("Feather");
    assertThat(list.get(1).getTrackNo()).isEqualTo(2);
    assertThat(list.get(1).getTitle()).isEqualTo("Luv Sic Part 3");
  }
}
