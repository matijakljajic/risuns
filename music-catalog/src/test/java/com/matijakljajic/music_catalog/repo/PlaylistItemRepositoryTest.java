package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.PlaylistItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class PlaylistItemRepositoryTest {

  @Autowired PlaylistItemRepository items;
  @Autowired PlaylistRepository playlists;
  @Autowired UserRepository users;

  @Test
  void drivingPlaylist_itemsAre1And2_forFeatherAndLuvSic3() {
    Long userId = users.findByUsername("matija").orElseThrow().getId();
    Long drivingId = playlists.findByUserIdAndName(userId, "Driving").orElseThrow().getId();

    List<PlaylistItem> list = items.findByPlaylistId(drivingId);
    assertThat(list).hasSize(2);

    var sorted = list.stream().sorted(Comparator.comparingInt(PlaylistItem::getPosition)).toList();
    assertThat(sorted.get(0).getPosition()).isEqualTo(1);
    assertThat(sorted.get(1).getPosition()).isEqualTo(2);

    assertThat(sorted.get(0).getTrack().getTitle()).isEqualTo("Feather");
    assertThat(sorted.get(1).getTrack().getTitle()).isEqualTo("Luv Sic Part 3");
  }
}
