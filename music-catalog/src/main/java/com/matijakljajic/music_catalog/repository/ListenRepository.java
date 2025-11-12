package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Listen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ListenRepository extends JpaRepository<Listen, Long> {
  
  List<Listen> findByUserUsernameOrderByListenedAtDesc(String username, Pageable pageable);

  List<Listen> findByUserIdOrderByListenedAtDesc(Long userId);

  @EntityGraph(attributePaths = {"track","track.album","track.album.primaryArtist","track.features","track.features.artist"})
  List<Listen> findByUserIdAndListenedAtGreaterThanEqualOrderByListenedAtDesc(Long userId, Instant since);

  @EntityGraph(attributePaths = {
    "user",
    "track",
    "track.album",
    "track.album.primaryArtist",
    "track.features",
    "track.features.artist"
  })
  List<Listen> findAllByOrderByListenedAtDesc(Pageable pageable);

  @Query("""
    select cast(l.listenedAt as date), count(l)
    from Listen l
    group by cast(l.listenedAt as date)
    order by cast(l.listenedAt as date)
  """)
  List<Object[]> dailyListens();

  @Query("""
    select l.track, count(l)
    from Listen l
    where l.listenedAt between :from and :to
    group by l.track
    order by count(l) desc
  """)
  List<Object[]> topTracks(Instant from, Instant to);

  @Query("""
    select l.user, count(l)
    from Listen l
    where l.track.album.id = :albumId
    group by l.user
    order by count(l) desc
  """)
  List<Object[]> topListenersForAlbum(@Param("albumId") Long albumId, Pageable pageable);

  @Query("""
    select l.user, count(l)
    from Listen l
    where l.track.id in (
      select pi.track.id from PlaylistItem pi
      where pi.playlist.id = :playlistId
    )
    group by l.user
    order by count(l) desc
  """)
  List<Object[]> topListenersForPlaylist(@Param("playlistId") Long playlistId, Pageable pageable);
}
