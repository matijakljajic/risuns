package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Listen;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class ListenRepositoryTest {

  @Autowired ListenRepository listens;

  @Test
  void history_for_matija_desc() {
    List<Listen> page = listens.findByUserUsernameOrderByListenedAtDesc("matija", PageRequest.of(0, 5));
    assertThat(page.size()).isBetween(0, 5);
    for (int i = 1; i < page.size(); i++) {
      assertThat(page.get(i-1).getListenedAt()).isAfterOrEqualTo(page.get(i).getListenedAt());
    }
  }

  @Test
  void dailyListens_hasAtLeastOneBucket() {
    var buckets = listens.dailyListens();
    assertThat(buckets).isNotEmpty();
    // each row: [java.sql.Date day, Long count]
    for (Object[] row : buckets) {
      assertThat(row[0]).isInstanceOf(java.sql.Date.class);
      assertThat(((Number) row[1]).longValue()).isGreaterThanOrEqualTo(1L);
    }
  }

  @Test
  void topTracks_lastYear_containsFeatherAtLeastOnce() {
    Instant to = Instant.now();
    Instant from = to.minus(365, ChronoUnit.DAYS);
    var rows = listens.topTracks(from, to);
    // row: [Track t, Long cnt]
    assertThat(rows).anySatisfy(r -> {
      var track = (com.matijakljajic.music_catalog.model.Track) r[0];
      var cnt   = ((Number) r[1]).longValue();
      if ("Feather".equals(track.getTitle())) {
        assertThat(cnt).isGreaterThanOrEqualTo(1L);
      }
    });
  }
}
