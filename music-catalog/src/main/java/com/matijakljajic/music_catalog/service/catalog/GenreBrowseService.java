package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreBrowseService {

  private final GenreRepository genres;

  public List<Genre> allGenres() {
    return genres.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }
}
