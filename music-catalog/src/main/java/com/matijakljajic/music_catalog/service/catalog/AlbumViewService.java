package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import com.matijakljajic.music_catalog.service.listening.ListeningStatsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumViewService {

  private final AlbumRepository albums;
  private final TrackRepository tracks;
  private final ListeningStatsService listeningStats;

  public AlbumView getAlbum(Long albumId) {
    var album = albums.findById(albumId).orElseThrow();
    var albumTracks = tracks.findByAlbumIdOrderByTrackNoAsc(albumId);
    var topListeners = listeningStats.topListenersForAlbum(albumId);
    return new AlbumView(album, albumTracks, topListeners);
  }

  @Getter
  @AllArgsConstructor
  public static class AlbumView {
    private final Album album;
    private final List<Track> tracks;
    private final List<ListeningStatsService.TopListener> topListeners;
  }
}
