package com.matijakljajic.music_catalog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class GenreRepositoryTest {

  @Autowired GenreRepository genres;

  @Test
  void findByNameIgnoreCase_hiphop() {
    var g = genres.findByNameIgnoreCase("hip-hop");
    assertThat(g).isPresent();
    assertThat(g.get().getName()).isEqualTo("Hip-Hop");
  }

  @Test
  void findByNameIgnoreCase_jazzRap() {
    var g = genres.findByNameIgnoreCase("jazz rap");
    assertThat(g).isPresent();
    assertThat(g.get().getName()).isEqualTo("Jazz Rap");
  }
}
