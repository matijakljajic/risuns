package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class ArtistRepositoryTest {

  @Autowired ArtistRepository artists;

  @Test
  void findByNameIgnoreCase_nujabes() {
    var a = artists.findByNameIgnoreCase("nujabes");
    assertThat(a).isPresent();
    assertThat(a.get().getName()).isEqualTo("Nujabes");
  }

  @Test
  void findByNameContainingIgnoreCase_ujab_containsNujabes() {
    var list = artists.findByNameContainingIgnoreCase("ujab");
    assertThat(list).extracting(Artist::getName).contains("Nujabes");
  }
}
