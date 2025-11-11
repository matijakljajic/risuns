package com.matijakljajic.music_catalog.service.listening;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.ListenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListeningStatsServiceTest {

  @Mock
  private ListenRepository listens;
  @InjectMocks
  private ListeningStatsService service;

  @Test
  void topListenersForAlbum_mapsRowsToDto() {
    User user = user(1L, "Alice", "alice");
    List<Object[]> rows = new ArrayList<>();
    rows.add(new Object[]{user, 4L});

    when(listens.topListenersForAlbum(5L, PageRequest.of(0, 5))).thenReturn(rows);

    var results = service.topListenersForAlbum(5L);

    assertThat(results).hasSize(1);
    var stat = results.getFirst();
    assertThat(stat.getUserId()).isEqualTo(1L);
    assertThat(stat.getLabel()).isEqualTo("Alice");
    assertThat(stat.getPlays()).isEqualTo(4L);
  }

  @Test
  void topListenersForPlaylist_fallsBackToUsernameWhenDisplayNameMissing() {
    User user = user(2L, null, "bob");
    List<Object[]> rows = new ArrayList<>();
    rows.add(new Object[]{user, 2L});

    when(listens.topListenersForPlaylist(9L, PageRequest.of(0, 5))).thenReturn(rows);

    var results = service.topListenersForPlaylist(9L);

    assertThat(results).hasSize(1);
    assertThat(results.getFirst().getLabel()).isEqualTo("bob");
  }

  private static User user(Long id, String displayName, String username) {
    User user = new User();
    user.setId(id);
    user.setDisplayName(displayName);
    user.setUsername(username);
    return user;
  }
}
