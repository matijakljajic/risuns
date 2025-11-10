package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

  Optional<Artist> findByNameIgnoreCase(String name);

  List<Artist> findByNameContainingIgnoreCase(String q);
}
