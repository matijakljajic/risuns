package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminGenreService {

  private final GenreRepository genres;

  public List<Genre> findAll() {
    return genres.findAll();
  }

  public Genre get(Long id) {
    return genres.findById(id).orElseThrow();
  }

  @Transactional
  public Genre create(Genre genre) {
    genre.setId(null);
    return genres.save(genre);
  }

  @Transactional
  public Genre update(Long id, Genre genre) {
    genre.setId(id);
    return genres.save(genre);
  }

  @Transactional
  public void delete(Long id) {
    genres.deleteById(id);
  }
}
