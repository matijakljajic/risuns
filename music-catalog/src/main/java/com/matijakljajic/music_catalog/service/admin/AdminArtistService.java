package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminArtistService {

  private final ArtistRepository artists;

  public List<Artist> findAll() {
    return artists.findAll();
  }

  public List<Artist> search(String query) {
    if (!StringUtils.hasText(query)) {
      return findAll();
    }
    return artists.findByNameContainingIgnoreCase(query.trim());
  }

  public Artist get(Long id) {
    return artists.findById(id).orElseThrow();
  }

  @Transactional
  public Artist create(Artist artist) {
    artist.setId(null);
    return artists.save(artist);
  }

  @Transactional
  public Artist update(Long id, Artist artist) {
    artist.setId(id);
    return artists.save(artist);
  }

  @Transactional
  public void delete(Long id) {
    artists.deleteById(id);
  }
}
