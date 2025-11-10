package com.matijakljajic.music_catalog;

import com.matijakljajic.music_catalog.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
class ReferentialIntegrityTest {

  @Autowired EntityManager em;

  @Test
  void cannot_delete_album_with_tracks() {
    Album album = em.createQuery("select a from Album a where a.title='Modal Soul'", Album.class).getSingleResult();
    assertThrows(PersistenceException.class, () -> {
      em.remove(album);
      em.flush(); // should fail because TRACK references ALBUM
    });
  }

  @Test
  void cannot_delete_artist_if_referenced() {
    Artist nujabes = em.createQuery("select a from Artist a where a.name='Nujabes'", Artist.class).getSingleResult();
    assertThrows(PersistenceException.class, () -> {
      em.remove(nujabes);
      em.flush();
    });
  }
}
