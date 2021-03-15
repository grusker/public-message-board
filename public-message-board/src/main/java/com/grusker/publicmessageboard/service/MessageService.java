package com.grusker.publicmessageboard.service;

import com.grusker.publicmessageboard.dto.MessageInputDto;
import com.grusker.publicmessageboard.dto.MessageOutputDto;
import com.grusker.publicmessageboard.entity.MessageEntity;
import com.grusker.publicmessageboard.exception.MessageNotFoundException;
import com.grusker.publicmessageboard.exception.UserNotAuthorizedException;
import com.grusker.publicmessageboard.mapper.MessageMapper;
import com.grusker.publicmessageboard.repository.MessageRepository;
import com.grusker.publicmessageboard.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageOutputDto createMessage(MessageInputDto messageInputDto) {
        MessageEntity messageEntity = MessageMapper.INSTANCE.toMessageEntity(messageInputDto, SecurityUtil.getLoginUserName());
        messageEntity = messageRepository.save(messageEntity);
        return MessageMapper.INSTANCE.toMessageOutputDto(messageEntity);
    }

    public MessageOutputDto updateMessage(Long id, MessageInputDto messageInputDto) throws MessageNotFoundException, UserNotAuthorizedException {
        validateMessageExistAndCreatedByUser(id);
        MessageEntity messageEntity = MessageMapper.INSTANCE.toMessageEntity(id, messageInputDto, SecurityUtil.getLoginUserName());
        messageEntity = messageRepository.save(messageEntity);
        return MessageMapper.INSTANCE.toMessageOutputDto(messageEntity);

    }

    public void deleteMessage(Long id) throws MessageNotFoundException, UserNotAuthorizedException {
        validateMessageExistAndCreatedByUser(id);
        messageRepository.deleteById(id);
    }

    public List<MessageOutputDto> getMessages() {
        List<MessageEntity> messageEntities = messageRepository.findAll();
        return MessageMapper.INSTANCE.toMessageOutputDtos(messageEntities);
    }

    private void validateMessageExistAndCreatedByUser(Long id) throws MessageNotFoundException, UserNotAuthorizedException {
        Optional<MessageEntity> currentMessage = messageRepository.findById(id);
        if (currentMessage.isEmpty()) {
            throw new MessageNotFoundException();
        }
        if (!currentMessage.get().getCreateUserName().equals(SecurityUtil.getLoginUserName())) {
            throw new UserNotAuthorizedException();
        }
    }
}
