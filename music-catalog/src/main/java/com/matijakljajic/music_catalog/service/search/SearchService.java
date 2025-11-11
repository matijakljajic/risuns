package com.matijakljajic.music_catalog.service.search;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.Role;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.TrackFeature;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

  private final TrackRepository tracks;
  private final ArtistRepository artists;
  private final AlbumRepository albums;
  private final PlaylistRepository playlists;
  private final UserRepository users;

  public SearchResults searchGeneral(String query) {
    String trimmed = normalize(query);
    if (trimmed == null) {
      return SearchResults.empty();
    }
    List<TrackResult> trackMatches = mapTracks(tracks.search(trimmed, null, null));
    var artistMatches = artists.findByNameContainingIgnoreCase(trimmed).stream()
        .map(a -> new ArtistResult(a.getId(), a.getName()))
        .toList();
    var albumMatches = albums.findByTitleContainingIgnoreCase(trimmed).stream()
        .map(alb -> new AlbumResult(
            alb.getId(),
            alb.getTitle(),
            alb.getPrimaryArtist() != null ? alb.getPrimaryArtist().getId() : null,
            alb.getPrimaryArtist() != null ? alb.getPrimaryArtist().getName() : null))
        .toList();
    var playlistMatches = playlists.searchPublic(trimmed).stream()
        .map(pl -> new PlaylistResult(
            pl.getId(),
            pl.getName(),
            pl.getUser() != null ? pl.getUser().getId() : null,
            pl.getUser() != null ? pl.getUser().getUsername() : null))
        .toList();
    var userMatches = users.findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(trimmed, trimmed).stream()
        .map(u -> new UserResult(u.getId(), u.getUsername(), u.getDisplayName()))
        .toList();

    return new SearchResults(trackMatches, artistMatches, albumMatches, playlistMatches, userMatches);
  }

  public List<TrackResult> searchTracks(String query, Long genreId, Boolean explicit) {
    String trimmed = normalize(query);
    return mapTracks(tracks.search(trimmed, genreId, explicit));
  }

  public List<ArtistResult> searchArtists(String query) {
    String trimmed = normalize(query);
    if (trimmed == null) return List.of();
    return artists.findByNameContainingIgnoreCase(trimmed).stream()
        .map(a -> new ArtistResult(a.getId(), a.getName()))
        .toList();
  }

  public List<AlbumResult> searchAlbums(String query, Integer releaseYear) {
    String trimmed = normalize(query);
    if (trimmed == null && releaseYear == null) return List.of();
    return albums.search(trimmed, releaseYear).stream()
        .map(alb -> new AlbumResult(
            alb.getId(),
            alb.getTitle(),
            alb.getPrimaryArtist() != null ? alb.getPrimaryArtist().getId() : null,
            alb.getPrimaryArtist() != null ? alb.getPrimaryArtist().getName() : null))
        .toList();
  }

  public List<PlaylistResult> searchPlaylists(String query) {
    String trimmed = normalize(query);
    return playlists.searchPublic(trimmed).stream()
        .map(pl -> new PlaylistResult(
            pl.getId(),
            pl.getName(),
            pl.getUser() != null ? pl.getUser().getId() : null,
            pl.getUser() != null ? pl.getUser().getUsername() : null))
        .toList();
  }

  public List<UserResult> searchUsers(String query, Role role, Boolean enabled) {
    String trimmed = normalize(query);
    if (trimmed == null && role == null && enabled == null) return List.of();
    return users.search(trimmed, role, enabled).stream()
        .map(u -> new UserResult(u.getId(), u.getUsername(), u.getDisplayName()))
        .toList();
  }

  private List<TrackResult> mapTracks(List<Track> trackEntities) {
    return trackEntities.stream()
        .map(track -> {
          var album = track.getAlbum();
          var primaryArtist = album.getPrimaryArtist();
          List<ArtistRef> featured = dedupeArtists(track.getFeatures());
          List<GenreRef> genreRefs = track.getGenres()
              .stream()
              .sorted(Comparator.comparing(Genre::getName, String.CASE_INSENSITIVE_ORDER))
              .map(g -> new GenreRef(g.getId(), g.getName()))
              .collect(Collectors.toList());
          return new TrackResult(
              track.getId(),
              track.getTitle(),
              album.getId(),
              album.getTitle(),
              new ArtistRef(primaryArtist.getId(), primaryArtist.getName()),
              featured,
              genreRefs
          );
        })
        .toList();
  }

  private List<ArtistRef> dedupeArtists(List<TrackFeature> features) {
    Map<Long, ArtistRef> ordered = new LinkedHashMap<>();
    features.stream()
        .sorted(Comparator.comparing(
            TrackFeature::getCreditOrder,
            Comparator.nullsLast(Integer::compareTo)))
        .forEach(tf -> {
          var artist = tf.getArtist();
          if (artist != null && artist.getId() != null) {
            ordered.putIfAbsent(artist.getId(), new ArtistRef(artist.getId(), artist.getName()));
          }
        });
    return ordered.values().stream().collect(Collectors.toList());
  }

  private String normalize(String input) {
    return (input != null && !input.isBlank()) ? input.trim() : null;
  }

  @Getter
  @AllArgsConstructor
  public static class SearchResults {
    private final List<TrackResult> trackMatches;
    private final List<ArtistResult> artistMatches;
    private final List<AlbumResult> albumMatches;
    private final List<PlaylistResult> playlistMatches;
    private final List<UserResult> userMatches;

    public boolean isEmpty() {
      return trackMatches.isEmpty() && artistMatches.isEmpty() && albumMatches.isEmpty()
          && playlistMatches.isEmpty() && userMatches.isEmpty();
    }

    public boolean isEmptyResult() {
      return isEmpty();
    }

    public static SearchResults empty() {
      return new SearchResults(List.of(), List.of(), List.of(), List.of(), List.of());
    }
  }

  @Getter
  @AllArgsConstructor
  public static class TrackResult {
    private final Long trackId;
    private final String trackTitle;
    private final Long albumId;
    private final String albumTitle;
    private final ArtistRef primaryArtist;
    private final List<ArtistRef> featuredArtists;
    private final List<GenreRef> genres;
  }

  @Getter
  @AllArgsConstructor
  public static class ArtistRef {
    private final Long id;
    private final String name;
  }

  @Getter
  @AllArgsConstructor
  public static class GenreRef {
    private final Long id;
    private final String name;
  }

  @Getter
  @AllArgsConstructor
  public static class ArtistResult {
    private final Long id;
    private final String name;
  }

  @Getter
  @AllArgsConstructor
  public static class AlbumResult {
    private final Long id;
    private final String title;
    private final Long primaryArtistId;
    private final String primaryArtistName;
  }

  @Getter
  @AllArgsConstructor
  public static class PlaylistResult {
    private final Long id;
    private final String name;
    private final Long ownerId;
    private final String ownerUsername;
  }

  @Getter
  @AllArgsConstructor
  public static class UserResult {
    private final Long id;
    private final String username;
    private final String displayName;
  }
}
