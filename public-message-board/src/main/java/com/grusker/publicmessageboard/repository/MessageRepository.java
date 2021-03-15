package com.grusker.publicmessageboard.repository;

import com.grusker.publicmessageboard.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
