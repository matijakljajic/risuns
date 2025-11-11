package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.TrackFeature;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
  private final ArtistRepository artists;

  public List<Track> findAll() {
    return tracks.findAll();
  }

  public List<Track> search(String query, Long genreId, Boolean explicit) {
    String trimmed = StringUtils.hasText(query) ? query.trim() : null;
    if (trimmed == null && genreId == null && explicit == null) {
      return findAll();
    }
    return tracks.search(trimmed, genreId, explicit);
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

  public List<Artist> allArtists() {
    return artists.findAll();
  }

  @Transactional
  public Track create(Track track, List<Long> genreIds, List<Long> featureArtistIds) {
    track.setId(null);
    track.setAlbum(resolveAlbum(track.getAlbum()));
    applyGenres(track, genreIds);
    applyFeatures(track, featureArtistIds);
    return tracks.save(track);
  }

  @Transactional
  public Track update(Long id, Track updated, List<Long> genreIds, List<Long> featureArtistIds) {
    Track managed = tracks.findById(id).orElseThrow();
    managed.setTitle(updated.getTitle());
    managed.setTrackNo(updated.getTrackNo());
    managed.setExplicit(updated.isExplicit());
    managed.setAlbum(resolveAlbum(updated.getAlbum()));
    applyGenres(managed, genreIds);
    applyFeatures(managed, featureArtistIds);
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

  private void applyFeatures(Track track, List<Long> artistIds) {
    track.getFeatures().clear();
    if (artistIds == null || artistIds.isEmpty()) {
      return;
    }
    int order = 0;
    for (Long artistId : artistIds) {
      Artist artist = artists.findById(artistId).orElseThrow();
      TrackFeature feature = new TrackFeature();
      feature.setTrack(track);
      feature.setArtist(artist);
      feature.setCreditOrder(order++);
      track.getFeatures().add(feature);
    }
  }
}
