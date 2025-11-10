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
class UserEntityTest {
    @Autowired EntityManager em;

    @Test
    @DisplayName("User: username and email must be unique")
    void user_uniques_and_helpers() {
        var u = new com.matijakljajic.music_catalog.model.User();
        u.setUsername("matija"); // duplicate username from seed
        u.setEmail("other@example.test");
        u.setPasswordHash("secret");
        u.setEnabled(true);
        u.setRole(com.matijakljajic.music_catalog.model.Role.USER);

        var ex = assertThrows(PersistenceException.class, () -> {
            em.persist(u);
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
