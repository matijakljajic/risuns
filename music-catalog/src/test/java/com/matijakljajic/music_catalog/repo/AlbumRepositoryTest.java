package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Album;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class AlbumRepositoryTest {

  @Autowired AlbumRepository albums;

  @Test
  void findByTitleIgnoreCase_findsModalSoul() {
    var a = albums.findByTitleIgnoreCase("modal soul");
    assertThat(a).isPresent();
    assertThat(a.get().getTitle()).isEqualTo("Modal Soul");
    assertThat(a.get().getPrimaryArtist()).isNotNull(); // via @EntityGraph if you use it
  }

  @Test
  void findByTitleContainingIgnoreCase_modal_returnsAtLeastOne() {
    List<Album> res = albums.findByTitleContainingIgnoreCase("modal");
    assertThat(res).extracting(Album::getTitle).contains("Modal Soul");
  }

  @Test
  void findByReleaseYear_2005_returnsOne() {
    List<Album> res = albums.findByReleaseYear(2005);
    assertThat(res).hasSize(1);
    assertThat(res.getFirst().getTitle()).isEqualTo("Modal Soul");
  }

  @Test
  void findByPrimaryArtistId_nujabes_hasTwoAlbums() {
    List<Album> res = albums.findByPrimaryArtistId(1L);
    assertThat(res).extracting(Album::getTitle)
        .containsExactlyInAnyOrder("Modal Soul", "Metaphorical Music");
  }
}
