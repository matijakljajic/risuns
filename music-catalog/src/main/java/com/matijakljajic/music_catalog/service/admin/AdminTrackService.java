package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTrackService {

  private final TrackRepository tracks;
  private final AlbumRepository albums;
  private final GenreRepository genres;

  public List<Track> findAll() {
    return tracks.findAll();
  }

  public Track get(Long id) {
    return tracks.findById(id).orElseThrow();
  }

  public List<Album> allAlbums() {
    return albums.findAll();
  }

  public List<Genre> allGenres() {
    return genres.findAll();
  }

  @Transactional
  public Track create(Track track, List<Long> genreIds) {
    track.setId(null);
    track.setAlbum(resolveAlbum(track.getAlbum()));
    applyGenres(track, genreIds);
    return tracks.save(track);
  }

  @Transactional
  public Track update(Long id, Track updated, List<Long> genreIds) {
    Track managed = tracks.findById(id).orElseThrow();
    managed.setTitle(updated.getTitle());
    managed.setTrackNo(updated.getTrackNo());
    managed.setExplicit(updated.isExplicit());
    managed.setAlbum(resolveAlbum(updated.getAlbum()));
    applyGenres(managed, genreIds);
    return managed;
  }

  @Transactional
  public void delete(Long id) {
    tracks.deleteById(id);
  }

  private Album resolveAlbum(Album albumRef) {
    if (albumRef == null || albumRef.getId() == null) {
      throw new IllegalArgumentException("Album is required");
    }
    return albums.findById(albumRef.getId()).orElseThrow();
  }

  private void applyGenres(Track track, List<Long> genreIds) {
    Set<Genre> newGenres = genreIds == null || genreIds.isEmpty()
        ? Collections.emptySet()
        : new LinkedHashSet<>(genres.findAllById(genreIds));
    track.getGenres().clear();
    track.getGenres().addAll(newGenres);
  }
}
