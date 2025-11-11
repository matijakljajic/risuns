package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreBrowseServiceTest {

  @Mock
  private GenreRepository genres;
  @InjectMocks
  private GenreBrowseService service;

  @Test
  void allGenres_delegatesToRepositoryWithAlphaSort() {
    List<Genre> expected = List.of(new Genre());
    when(genres.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(expected);

    assertThat(service.allGenres()).isSameAs(expected);
  }
}
