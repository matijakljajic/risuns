package com.matijakljajic.music_catalog.service.catalog;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistViewServiceTest {

  @Mock
  private ArtistRepository artists;
  @Mock
  private AlbumRepository albums;
  @InjectMocks
  private ArtistViewService service;

  @Test
  void getArtist_combinesArtistAndAlbums() {
    Artist artist = new Artist();
    artist.setId(8L);
    artist.setName("Tester");

    Album album = new Album();
    album.setId(55L);
    album.setPrimaryArtist(artist);
    album.setTitle("Debut");

    when(artists.findById(8L)).thenReturn(Optional.of(artist));
    when(albums.findByPrimaryArtistId(8L)).thenReturn(List.of(album));

    var view = service.getArtist(8L);

    assertThat(view.getArtist()).isSameAs(artist);
    assertThat(view.getAlbums()).containsExactly(album);
  }
}
