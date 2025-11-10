package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.TrackFeature;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackFeatureRepository extends JpaRepository<TrackFeature, Long> {

  List<TrackFeature> findByTrackIdOrderByCreditOrderAsc(Long trackId);

  boolean existsByTrackIdAndArtistId(Long trackId, Long artistId);

  Optional<TrackFeature> findByTrackIdAndArtistId(Long trackId, Long artistId);
}
