package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
class MessageRepositoryTest {

  @Autowired MessageRepository messages;

  @Test
  void inbox_matija_hasSaraMessage_descSorted() {
    List<Message> inbox = messages.findByReceiverUsernameOrderBySentAtDesc("matija");
    assertThat(inbox).isNotEmpty();
    assertThat(inbox.stream().anyMatch(m -> m.getContent().contains("Hey there!"))).isTrue();
    // check sort (DESC)
    for (int i = 1; i < inbox.size(); i++) {
      assertThat(inbox.get(i - 1).getSentAt()).isAfterOrEqualTo(inbox.get(i).getSentAt());
    }
  }

  @Test
  void outbox_matija_mayContainHi_descSorted() {
    List<Message> out = messages.findBySenderUsernameOrderBySentAtDesc("matija");
    // could be 1 row per seed (“Hi!” to Sara)
    for (int i = 1; i < out.size(); i++) {
      assertThat(out.get(i - 1).getSentAt()).isAfterOrEqualTo(out.get(i).getSentAt());
    }
  }
}
