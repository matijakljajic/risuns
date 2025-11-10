package com.matijakljajic.music_catalog.entity;

import com.matijakljajic.music_catalog.MusicCatalogApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MusicCatalogApplication.class)
@ActiveProfiles("h2")
@Transactional
class GenreAndFeaturesTest {
    @Autowired EntityManager em;

    @Test
    @DisplayName("Genre: name unique")
    void genre_unique_name() {
        var g = new com.matijakljajic.music_catalog.model.Genre();
        g.setName("Hip-Hop"); // already in seed
        var ex = assertThrows(PersistenceException.class, () -> {
            em.persist(g);
            em.flush();
        });
        assertTrue(hasConstraintViolation(ex));
    }

    @Test
    @DisplayName("Track features: (track, artist) unique and ordering is by credit_order")
    void track_features_respect_uniques_and_ordering() {
        var track = em.find(com.matijakljajic.music_catalog.model.Track.class, 1L); // Feather

        // verify ordering by JPQL (do not rely on helper methods)
        var ordered = em.createQuery(
            "select tf from com.matijakljajic.music_catalog.model.TrackFeature tf " +
            "where tf.track.id = :tid order by tf.creditOrder asc",
            com.matijakljajic.music_catalog.model.TrackFeature.class)
            .setParameter("tid", 1L)
            .getResultList();

        assertEquals(2, ordered.size());
        var names = ordered.stream().map(tf -> tf.getArtist().getName()).toList();
        assertEquals(List.of("Shing02", "Uyama Hiroto"), names);

        // (track, artist) unique pair
        var a2 = em.find(com.matijakljajic.music_catalog.model.Artist.class, 2L); // Shing02
        var dupPair = new com.matijakljajic.music_catalog.model.TrackFeature();
        dupPair.setTrack(track);
        dupPair.setArtist(a2);
        dupPair.setCreditOrder(3);

        var ex1 = assertThrows(PersistenceException.class, () -> {
            em.persist(dupPair);
            em.flush();
        });
        assertTrue(hasConstraintViolation(ex1));

        // (track, credit_order) unique
        var a3 = em.find(com.matijakljajic.music_catalog.model.Artist.class, 3L); // Uyama
        var dupOrder = new com.matijakljajic.music_catalog.model.TrackFeature();
        dupOrder.setTrack(track);
        dupOrder.setArtist(a3);
        dupOrder.setCreditOrder(1); // already used

        var ex2 = assertThrows(PersistenceException.class, () -> {
            em.persist(dupOrder);
            em.flush();
        });
        assertTrue(hasConstraintViolation(ex2));
    }

    private boolean hasConstraintViolation(Throwable t) {
        while (t != null) {
            if (t instanceof ConstraintViolationException) return true;
            t = t.getCause();
        }
        return false;
    }
}
