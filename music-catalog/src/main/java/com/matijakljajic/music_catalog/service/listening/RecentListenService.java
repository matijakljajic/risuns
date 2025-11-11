package com.matijakljajic.music_catalog.service.listening;

import com.matijakljajic.music_catalog.repository.ListenRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentListenService {

  private final ListenRepository listens;

  public List<RecentListenView> latest(int limit) {
    return listens.findAllByOrderByListenedAtDesc(PageRequest.of(0, limit))
        .stream()
        .map(l -> {
          var track = l.getTrack();
          var album = track.getAlbum();
          var primaryArtist = album.getPrimaryArtist();
          List<ArtistRef> featured = dedupeFeatures(track.getFeatures());
          return new RecentListenView(
              l.getUser().getId(),
              l.getUser().getUsername(),
              track.getId(),
              track.getTitle(),
              album.getId(),
              album.getTitle(),
              l.getListenedAt(),
              new ArtistRef(primaryArtist.getId(), primaryArtist.getName()),
              featured
          );
        })
        .toList();
  }

  @Getter
  @AllArgsConstructor
  public static class RecentListenView {
    private final Long userId;
    private final String username;
    private final Long trackId;
    private final String trackTitle;
    private final Long albumId;
    private final String albumTitle;
    private final Instant listenedAt;
    private final ArtistRef primaryArtist;
    private final List<ArtistRef> featuredArtists;
  }

  @Getter
  @AllArgsConstructor
  public static class ArtistRef {
    private final Long id;
    private final String name;
  }

  private List<ArtistRef> dedupeFeatures(List<com.matijakljajic.music_catalog.model.TrackFeature> features) {
    return features.stream()
        .filter(tf -> tf.getArtist() != null && tf.getArtist().getId() != null)
        .sorted(Comparator.comparing(
            com.matijakljajic.music_catalog.model.TrackFeature::getCreditOrder,
            Comparator.nullsLast(Integer::compareTo)))
        .collect(Collectors.toMap(
            tf -> tf.getArtist().getId(),
            tf -> new ArtistRef(tf.getArtist().getId(), tf.getArtist().getName()),
            (left, right) -> left,
            java.util.LinkedHashMap::new))
        .values()
        .stream()
        .collect(Collectors.toList());
  }
}
