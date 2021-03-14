package com.grusker.publicmessageboard.services;

import com.grusker.publicmessageboard.dtos.MessageInputDto;
import com.grusker.publicmessageboard.dtos.MessageOutputDto;
import com.grusker.publicmessageboard.entities.MessageEntity;
import com.grusker.publicmessageboard.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void createMessage_messageCreateSuccessfully() {
        MessageEntity expectedMessage = prepareMessageEntity((long) 1, "Created message");
        doReturn(expectedMessage).when(messageRepository).save(any(MessageEntity.class));

        MessageOutputDto responseMessage = messageService.createMessage(new MessageInputDto());
        assertNotNull(responseMessage);
        assertEquals(responseMessage.getContent(), expectedMessage.getContent());
        assertEquals(responseMessage.getId(), expectedMessage.getId());
    }

    @Test
    public void updateMessage_messageUpdatedSuccessfully() {
        doReturn(true).when(messageRepository).existsById(any(Long.class));

        MessageEntity expectedMessage = prepareMessageEntity((long) 1, "Updated message");
        doReturn(expectedMessage).when(messageRepository).save(any(MessageEntity.class));

        MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
        assertNotNull(responseMessage);
        assertEquals(responseMessage.getContent(), expectedMessage.getContent());
        assertEquals(responseMessage.getId(), expectedMessage.getId());
    }

    @Test
    public void deleteMessage_methodCalledOneTime() {
        doReturn(true).when(messageRepository).existsById(any(Long.class));
        doNothing().when(messageRepository).deleteById(any(Long.class));

        messageService.deleteMessage((long) 1);

        verify(messageRepository, times(1)).deleteById(eq((long)1));
    }

    @Test
    public void getMessages_returnedListWithThreeItems() {
        List<MessageEntity> expectedMessages = prepareMessageEntityList();
        doReturn(expectedMessages).when(messageRepository).findAll();

        List<MessageOutputDto> responseMessages = messageService.getMessages();
        assertFalse(CollectionUtils.isEmpty(responseMessages));
        assertTrue(responseMessages.size() == expectedMessages.size());
    }

    private List<MessageEntity> prepareMessageEntityList() {
        List<MessageEntity> messageEntities = new ArrayList<>();
        messageEntities.add(prepareMessageEntity((long) 1, "First message"));
        messageEntities.add(prepareMessageEntity((long) 2, "Second message"));
        return messageEntities;
    }

    private MessageEntity prepareMessageEntity(Long id, String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(id);
        messageEntity.setContent(content);
        return messageEntity;
    }


}
