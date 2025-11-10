package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

  @EntityGraph(attributePaths = {"primaryArtist"})
  Optional<Album> findByTitleIgnoreCase(String title);

  @EntityGraph(attributePaths = {"primaryArtist"})
  List<Album> findByTitleContainingIgnoreCase(String q);

  List<Album> findByPrimaryArtistId(Long artistId);

  List<Album> findByReleaseYear(Integer releaseYear);
}
