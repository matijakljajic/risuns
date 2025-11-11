package com.matijakljajic.music_catalog.service.listening;

import com.matijakljajic.music_catalog.model.TrackFeature;
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
          List<ArtistRef> featured = track.getFeatures().stream()
              .sorted(Comparator.comparing(
                  TrackFeature::getCreditOrder,
                  Comparator.nullsLast(Integer::compareTo)))
              .map(tf -> new ArtistRef(tf.getArtist().getId(), tf.getArtist().getName()))
              .collect(Collectors.toList());
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
}
