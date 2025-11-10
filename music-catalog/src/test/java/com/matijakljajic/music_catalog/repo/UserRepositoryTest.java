package com.matijakljajic.music_catalog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class UserRepositoryTest {

  @Autowired UserRepository users;

  @Test
  void findByUsername_matija_existsAndEnabled() {
    var u = users.findByUsername("matija");
    assertThat(u).isPresent();
    assertThat(u.get().isEnabled()).isTrue();
    assertThat(u.get().getEmail()).isEqualTo("matija@example.test");
  }
}
