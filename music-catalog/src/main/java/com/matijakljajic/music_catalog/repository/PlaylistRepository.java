package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
  List<Playlist> findByUserId(Long userId);
  List<Playlist> findByUserUsername(String username);

  Optional<Playlist> findByUserIdAndName(Long userId, String name);

  List<Playlist> findByNameContainingIgnoreCase(String q);

  @EntityGraph(attributePaths = {"user"})
  @Query("""
    select p from Playlist p
    where p.isPublic = true
      and (:q is null or lower(p.name) like lower(concat('%', :q, '%')))
    order by lower(p.name)
  """)
  List<Playlist> searchPublic(@Param("q") String query);
}
