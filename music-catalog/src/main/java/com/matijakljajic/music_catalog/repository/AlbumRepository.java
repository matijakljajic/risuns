package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @EntityGraph(attributePaths = {"primaryArtist"})
  @Query("""
    select a from Album a
    where (:q is null or lower(a.title) like lower(concat('%', :q, '%')))
      and (:year is null or a.releaseYear = :year)
    order by lower(a.title)
  """)
  List<Album> search(@Param("q") String query, @Param("year") Integer year);
}
