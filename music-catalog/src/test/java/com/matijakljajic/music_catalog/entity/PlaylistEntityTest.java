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
class PlaylistEntityTest {
    @Autowired EntityManager em;

    @Test
    @DisplayName("Playlist: name must be unique per user")
    void playlist_name_unique_per_user() {
        var user = em.find(com.matijakljajic.music_catalog.model.User.class, 1L);
        var p = new com.matijakljajic.music_catalog.model.Playlist();
        p.setUser(user);
        p.setName("Driving"); // already exists for user 1
        p.setPublic(false);
        p.setCreatedAt(Instant.now());

        var ex = assertThrows(PersistenceException.class, () -> {
            em.persist(p);
            em.flush();
        });
        assertTrue(hasConstraintViolation(ex));
    }

    @Test
    @DisplayName("Playlist items: ordering by position and unique constraints")
    void playlist_items_order_and_uniques() {
        var pl = em.find(com.matijakljajic.music_catalog.model.Playlist.class, 1L); // Driving

        var items = em.createQuery(
            "select pi from com.matijakljajic.music_catalog.model.PlaylistItem pi " +
            "where pi.playlist.id = :pid order by pi.position asc",
            com.matijakljajic.music_catalog.model.PlaylistItem.class)
            .setParameter("pid", 1L)
            .getResultList();

        assertEquals(2, items.size());
        assertEquals(List.of(1,2), items.stream().map(com.matijakljajic.music_catalog.model.PlaylistItem::getPosition).toList());
        assertEquals(List.of("Feather", "Luv Sic Part 3"),
            items.stream().map(it -> it.getTrack().getTitle()).toList());

        // duplicate track in same playlist -> constraint
        var dup = new com.matijakljajic.music_catalog.model.PlaylistItem();
        dup.setPlaylist(pl);
        dup.setTrack(em.find(com.matijakljajic.music_catalog.model.Track.class, 1L)); // Feather
        dup.setPosition(3);

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
