package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
  List<Playlist> findByUserId(Long userId);
  List<Playlist> findByUserUsername(String username);

  Optional<Playlist> findByUserIdAndName(Long userId, String name);
}
