package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.PlaylistItem;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long> {

  List<PlaylistItem> findByPlaylistId(Long playlistId);

  @EntityGraph(attributePaths = {
      "track",
      "track.album",
      "track.album.primaryArtist",
      "track.features",
      "track.features.artist"
  })
  List<PlaylistItem> findByPlaylistIdOrderByPositionAsc(Long playlistId);

  boolean existsByPlaylistIdAndTrackId(Long playlistId, Long trackId);

  @Query("select coalesce(max(pi.position), 0) from PlaylistItem pi where pi.playlist.id = :playlistId")
  Integer findMaxPosition(@Param("playlistId") Long playlistId);

  @Modifying
  @Transactional
  @Query("""
         update PlaylistItem pi
            set pi.position = pi.position + 1
          where pi.playlist.id = :playlistId
            and pi.position >= :fromPos
         """)
  int shiftPositionsUp(@Param("playlistId") Long playlistId, @Param("fromPos") int fromPos);

  @Modifying
  @Transactional
  @Query("""
         update PlaylistItem pi
            set pi.position = pi.position - 1
          where pi.playlist.id = :playlistId
            and pi.position > :fromPos
         """)
  int shiftPositionsDown(@Param("playlistId") Long playlistId, @Param("fromPos") int fromPos);
}
