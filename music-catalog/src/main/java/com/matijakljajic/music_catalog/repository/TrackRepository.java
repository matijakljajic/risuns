package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Track;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

  @EntityGraph(attributePaths = {"album","album.primaryArtist","genres","features","features.artist"})
  Optional<Track> findByTitleIgnoreCase(String title);

  @EntityGraph(attributePaths = {"album","album.primaryArtist","genres"})
  List<Track> findByTitleContainingIgnoreCase(String q);

  @EntityGraph(attributePaths = {"album","album.primaryArtist","genres"})
  List<Track> findByAlbumIdOrderByTrackNoAsc(Long albumId);

  @EntityGraph(attributePaths = {"album","album.primaryArtist","genres"})
  List<Track> findByGenresId(Long genreId);

  @EntityGraph(attributePaths = {
      "album",
      "album.primaryArtist",
      "genres",
      "features",
      "features.artist"
  })
  @Query("""
    select distinct t
    from Track t
    left join t.genres g
    where (:q is null or lower(t.title) like lower(concat('%', :q, '%')))
      and (:genreId is null or g.id = :genreId)
      and (:explicit is null or t.explicit = :explicit)
    order by lower(t.title)
  """)
  List<Track> search(@Param("q") String query,
                     @Param("genreId") Long genreId,
                     @Param("explicit") Boolean explicit);
}
