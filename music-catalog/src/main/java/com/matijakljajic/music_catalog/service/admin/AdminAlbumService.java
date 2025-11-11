package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAlbumService {

  private final AlbumRepository albums;
  private final ArtistRepository artists;

  public List<Album> findAll() {
    return albums.findAll();
  }

  public List<Album> search(String query, Integer year) {
    String trimmed = StringUtils.hasText(query) ? query.trim() : null;
    if (trimmed == null && year == null) {
      return findAll();
    }
    return albums.search(trimmed, year);
  }

  public Album get(Long id) {
    return albums.findById(id).orElseThrow();
  }

  public List<Artist> allArtists() {
    return artists.findAll();
  }

  @Transactional
  public Album create(Album album) {
    attachPrimaryArtist(album);
    album.setId(null);
    return albums.save(album);
  }

  @Transactional
  public Album update(Long id, Album album) {
    attachPrimaryArtist(album);
    album.setId(id);
    return albums.save(album);
  }

  @Transactional
  public void delete(Long id) {
    albums.deleteById(id);
  }

  private void attachPrimaryArtist(Album album) {
    Artist artist = album.getPrimaryArtist();
    if (artist == null || artist.getId() == null) {
      throw new IllegalArgumentException("Primary artist is required");
    }
    album.setPrimaryArtist(artists.findById(artist.getId()).orElseThrow());
  }
}
