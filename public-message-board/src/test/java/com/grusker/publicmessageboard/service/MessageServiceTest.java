package com.grusker.publicmessageboard.service;

import com.grusker.publicmessageboard.dto.MessageInputDto;
import com.grusker.publicmessageboard.dto.MessageOutputDto;
import com.grusker.publicmessageboard.entity.MessageEntity;
import com.grusker.publicmessageboard.exception.MessageNotFoundException;
import com.grusker.publicmessageboard.exception.UserNotAuthorizedException;
import com.grusker.publicmessageboard.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Admin", password = "admin")
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void createMessage_messageCreateSuccessfully() {
        MessageEntity expectedMessage = prepareMessageEntity((long) 1, "Created message", "Admin");
        doReturn(expectedMessage).when(messageRepository).save(any(MessageEntity.class));

        MessageOutputDto responseMessage = messageService.createMessage(new MessageInputDto());
        assertNotNull(responseMessage);
        assertEquals(responseMessage.getContent(), expectedMessage.getContent());
        assertEquals(responseMessage.getId(), expectedMessage.getId());
    }

    @Test
    public void updateMessage_IfMessageExistAndCreatedByGivenUser() throws MessageNotFoundException, UserNotAuthorizedException {
        MessageEntity currentMessage = prepareMessageEntity((long) 1, "Current message", "Admin");
        doReturn(Optional.of(currentMessage)).when(messageRepository).findById(any(Long.class));

        MessageEntity expectedMessage = prepareMessageEntity((long) 1, "Updated message", "Admin");
        doReturn(expectedMessage).when(messageRepository).save(any(MessageEntity.class));

        MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
        assertNotNull(responseMessage);
        assertEquals(responseMessage.getContent(), expectedMessage.getContent());
        assertEquals(responseMessage.getId(), expectedMessage.getId());
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void updateMessage_IfMessageExistButCreatedByDifferentUser() {
        MessageEntity currentMessage = prepareMessageEntity((long) 1, "Current message", "Admin");
        doReturn(Optional.of(currentMessage)).when(messageRepository).findById(any(Long.class));
        try {
            MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
            assertFalse(responseMessage == null);
        } catch (Exception ex) {
            assertTrue(ex instanceof UserNotAuthorizedException);
        }
    }

    @Test
    public void updateMessage_IfMessageDoesNotExist() {
        doReturn(Optional.empty()).when(messageRepository).findById(any(Long.class));
        try {
            MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
            assertFalse(responseMessage == null);
        } catch (Exception ex) {
            assertTrue(ex instanceof MessageNotFoundException);
        }
    }

    @Test
    public void deleteMessage_DeleteByIdCalledOneTime() throws MessageNotFoundException, UserNotAuthorizedException {
        MessageEntity currentMessage = prepareMessageEntity((long) 1, "Current message", "Admin");
        doReturn(Optional.of(currentMessage)).when(messageRepository).findById(any(Long.class));
        doNothing().when(messageRepository).deleteById(any(Long.class));

        messageService.deleteMessage((long) 1);

        verify(messageRepository, times(1)).deleteById(eq((long)1));
    }

    @Test
    @WithMockUser(username = "User", password = "user")
    public void deleteMessage_IfMessageExistButCreatedByDifferentUser() {
        MessageEntity currentMessage = prepareMessageEntity((long) 1, "Current message", "Admin");
        doReturn(Optional.of(currentMessage)).when(messageRepository).findById(any(Long.class));
        try {
            MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
            assertFalse(responseMessage == null);
        } catch (Exception ex) {
            assertTrue(ex instanceof UserNotAuthorizedException);
        }
    }

    @Test
    public void deleteMessage_IfMessageDoesNotExist() {
        doReturn(Optional.empty()).when(messageRepository).findById(any(Long.class));
        try {
            MessageOutputDto responseMessage = messageService.updateMessage((long) 1, new MessageInputDto());
            assertFalse(responseMessage == null);
        } catch (Exception ex) {
            assertTrue(ex instanceof MessageNotFoundException);
        }
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
        messageEntities.add(prepareMessageEntity((long) 1, "First message", "Admin"));
        messageEntities.add(prepareMessageEntity((long) 2, "Second message", "Admin"));
        return messageEntities;
    }

    private MessageEntity prepareMessageEntity(Long id, String content, String loginUserName) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(id);
        messageEntity.setContent(content);
        messageEntity.setCreateUserName(loginUserName);
        return messageEntity;
    }


}
