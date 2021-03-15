package com.grusker.publicmessageboard.services;

import com.grusker.publicmessageboard.dtos.MessageInputDto;
import com.grusker.publicmessageboard.dtos.MessageOutputDto;
import com.grusker.publicmessageboard.entities.MessageEntity;
import com.grusker.publicmessageboard.mappers.MessageMapper;
import com.grusker.publicmessageboard.repositories.MessageRepository;
import com.grusker.publicmessageboard.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageOutputDto createMessage(MessageInputDto messageInputDto) {
        MessageEntity messageEntity = MessageMapper.INSTANCE.toMessageEntity(messageInputDto, SecurityUtil.getLoginUserName());
        messageEntity = messageRepository.save(messageEntity);
        return MessageMapper.INSTANCE.toMessageOutputDto(messageEntity);
    }

    public MessageOutputDto updateMessage(Long id, MessageInputDto messageInputDto) {
        if (messageRepository.existsById(id)) {
            MessageEntity messageEntity = MessageMapper.INSTANCE.toMessageEntity(id, messageInputDto, SecurityUtil.getLoginUserName());
            messageEntity = messageRepository.save(messageEntity);
            return MessageMapper.INSTANCE.toMessageOutputDto(messageEntity);
        }
        return null;
    }

    public void deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        }
    }

    public List<MessageOutputDto> getMessages() {
        List<MessageEntity> messageEntities = messageRepository.findAll();
        return MessageMapper.INSTANCE.toMessageOutputDtos(messageEntities);
    }
}
