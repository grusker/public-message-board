package com.grusker.publicmessageboard.repositories;

import com.grusker.publicmessageboard.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
