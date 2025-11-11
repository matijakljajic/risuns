package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Message;
import com.matijakljajic.music_catalog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findByReceiverUsernameOrderBySentAtDesc(String username);
  List<Message> findBySenderUsernameOrderBySentAtDesc(String username);

  List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);
  List<Message> findBySenderIdOrderBySentAtDesc(Long senderId);

  Page<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId, Pageable pageable);
  Page<Message> findByReceiverIdAndSenderIdOrderBySentAtDesc(Long receiverId, Long senderId, Pageable pageable);
  Page<Message> findBySenderIdOrderBySentAtDesc(Long senderId, Pageable pageable);
  Page<Message> findBySenderIdAndReceiverIdOrderBySentAtDesc(Long senderId, Long receiverId, Pageable pageable);

  @Query("""
      select distinct m.sender
      from Message m
      where m.receiver.id = :userId
      order by lower(m.sender.username)
      """)
  List<User> findDistinctSendersByReceiverId(@Param("userId") Long userId);

  @Query("""
      select distinct m.receiver
      from Message m
      where m.sender.id = :userId
      order by lower(m.receiver.username)
      """)
  List<User> findDistinctReceiversBySenderId(@Param("userId") Long userId);

  @Query("""
      select m from Message m
      where (m.sender.id = :currentId and m.receiver.id = :otherId)
         or (m.sender.id = :otherId and m.receiver.id = :currentId)
      order by m.sentAt desc
      """)
  Page<Message> findConversationPage(@Param("currentId") Long currentId,
                                     @Param("otherId") Long otherId,
                                     Pageable pageable);
}
