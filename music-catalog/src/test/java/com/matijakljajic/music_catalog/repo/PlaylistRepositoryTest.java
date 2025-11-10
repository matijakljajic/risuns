package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Playlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class PlaylistRepositoryTest {

  @Autowired PlaylistRepository playlists;
  @Autowired UserRepository users;

  @Test
  void findByUserUsername_matija_hasDrivingAndStudy() {
    List<Playlist> list = playlists.findByUserUsername("matija");
    assertThat(list).extracting(Playlist::getName)
        .containsExactlyInAnyOrder("Driving", "Study");
  }

  @Test
  void findByUserIdAndName_driving_found() {
    Long userId = users.findByUsername("matija").orElseThrow().getId();
    var p = playlists.findByUserIdAndName(userId, "Driving");
    assertThat(p).isPresent();
    assertThat(p.get().isPublic()).isFalse();
  }
}
