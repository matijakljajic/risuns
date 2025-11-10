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
class MessageAndListenEntityTest {
    @Autowired EntityManager em;

    @Test
    @DisplayName("Listen: persists and can be queried")
    void listen_persists() {
        var u = em.find(com.matijakljajic.music_catalog.model.User.class, 1L);
        var t = em.find(com.matijakljajic.music_catalog.model.Track.class, 1L);

        var l = new com.matijakljajic.music_catalog.model.Listen();
        l.setUser(u);
        l.setTrack(t);
        l.setListenedAt(Instant.now());

        em.persist(l);
        em.flush();

        var list = em.createQuery(
            "select l from com.matijakljajic.music_catalog.model.Listen l where l.user.id=:uid order by l.listenedAt desc",
            com.matijakljajic.music_catalog.model.Listen.class)
            .setParameter("uid", 1L).getResultList();

        assertFalse(list.isEmpty());
        assertEquals(u.getId(), list.get(0).getUser().getId());
    }

    @Test
    @DisplayName("Message: persists with sender/receiver")
    void message_persists() {
        var sender = em.find(com.matijakljajic.music_catalog.model.User.class, 1L);
        var receiver = em.find(com.matijakljajic.music_catalog.model.User.class, 2L);

        var m = new com.matijakljajic.music_catalog.model.Message();
        m.setSender(sender);
        m.setReceiver(receiver);
        m.setContent("Ping");
        m.setSentAt(Instant.now());

        em.persist(m);
        em.flush();

        var inbox = em.createQuery(
            "select m from com.matijakljajic.music_catalog.model.Message m where m.receiver.id=:rid order by m.sentAt desc",
            com.matijakljajic.music_catalog.model.Message.class)
            .setParameter("rid", 2L).getResultList();

        assertTrue(inbox.stream().anyMatch(mm -> mm.getContent().equals("Ping")));
    }
}
