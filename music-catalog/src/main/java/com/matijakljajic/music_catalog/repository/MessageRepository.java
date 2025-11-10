package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findByReceiverUsernameOrderBySentAtDesc(String username);
  List<Message> findBySenderUsernameOrderBySentAtDesc(String username);

  List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);
  List<Message> findBySenderIdOrderBySentAtDesc(Long senderId);
}
