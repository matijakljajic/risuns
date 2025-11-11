package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistViewService {

  private final ArtistRepository artists;
  private final AlbumRepository albums;

  public ArtistView getArtist(Long id) {
    Artist artist = artists.findById(id).orElseThrow();
    List<Album> catalog = albums.findByPrimaryArtistId(id);
    return new ArtistView(artist, catalog);
  }

  @Getter
  @AllArgsConstructor
  public static class ArtistView {
    private final Artist artist;
    private final List<Album> albums;
  }
}
