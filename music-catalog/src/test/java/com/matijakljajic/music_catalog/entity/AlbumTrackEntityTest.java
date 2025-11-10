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
class AlbumTrackEntityTest {
    @Autowired EntityManager em;

    @Test
    @DisplayName("Album: (artist,title,year) must be unique")
    void album_unique_artist_title_year() {
        var artist = em.find(com.matijakljajic.music_catalog.model.Artist.class, 1L); // Nujabes
        var dup = new com.matijakljajic.music_catalog.model.Album();
        dup.setTitle("Modal Soul");
        dup.setReleaseYear(2005);
        dup.setPrimaryArtist(artist);

        var ex = assertThrows(PersistenceException.class, () -> {
            em.persist(dup);
            em.flush();
        });
        // hibernate wraps as PersistenceException -> ConstraintViolationException
        assertTrue(hasConstraintViolation(ex));
    }

    @Test
    @DisplayName("Track: (album, track_no) unique per album")
    void track_unique_trackno_per_album() {
        var album = em.find(com.matijakljajic.music_catalog.model.Album.class, 1L); // Modal Soul
        var dup = new com.matijakljajic.music_catalog.model.Track();
        dup.setAlbum(album);
        dup.setTitle("Another #1");
        dup.setTrackNo(1);
        dup.setExplicit(false);

        var ex = assertThrows(PersistenceException.class, () -> {
            em.persist(dup);
            em.flush();
        });
        assertTrue(hasConstraintViolation(ex));
    }

    private boolean hasConstraintViolation(Throwable t) {
        while (t != null) {
            if (t instanceof ConstraintViolationException) return true;
            t = t.getCause();
        }
        return false;
    }
}
