package com.grusker.publicmessageboard.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grusker.publicmessageboard.dtos.MessageInputDto;
import com.grusker.publicmessageboard.dtos.MessageOutputDto;
import com.grusker.publicmessageboard.services.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(controllers = MessageController.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private MessageService messageService;

    @Test
    public void createMessage_isCreated() throws Exception {
        MessageOutputDto expectedMessageOutputDto = prepareMessageOutputDto((long) 1, "Created message");
        doReturn(expectedMessageOutputDto).when(messageService).createMessage(any(MessageInputDto.class));

        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setContent("Created message");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsBytes(messageInputDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        MessageOutputDto messageOutputDto = objMapper.readValue(response, MessageOutputDto.class);
        assertEquals(messageInputDto.getContent(), messageOutputDto.getContent());
    }

    @Test
    public void updateMessageById_isOk() throws Exception {
        MessageOutputDto expectedMessageOutputDto = prepareMessageOutputDto((long) 1, "Updated message");

        doReturn(expectedMessageOutputDto).when(messageService).updateMessage(any(Long.class), any(MessageInputDto.class));

        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setContent("Updated message");
        String messageId = "1";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/messages/" + messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsBytes(messageInputDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        MessageOutputDto messageOutputDto = objMapper.readValue(response, MessageOutputDto.class);
        assertEquals(messageInputDto.getContent(), messageOutputDto.getContent());
    }

    @Test
    public void deleteMessageById_isOk() throws Exception {
        doNothing().when(messageService).deleteMessage(any(Long.class));

        String messageId = "1";
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/" + messageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllMessages_isOk() throws Exception {
        List<MessageOutputDto> expectedMessageDtoList = prepareMessageOutputDtoList();
        doReturn(expectedMessageDtoList).when(messageService).getMessages();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<MessageOutputDto> messageOutputDtoList = Arrays.asList(objMapper.readValue(response, MessageOutputDto[].class));
        assertTrue(messageOutputDtoList.size() == expectedMessageDtoList.size());
    }

    private List<MessageOutputDto> prepareMessageOutputDtoList() {
        List<MessageOutputDto> messageOutputDtoList = new ArrayList<>();
        messageOutputDtoList.add(prepareMessageOutputDto((long) 1, "First message"));
        messageOutputDtoList.add(prepareMessageOutputDto((long) 2, "Second message"));
        return messageOutputDtoList;
    }

    private MessageOutputDto prepareMessageOutputDto(Long id, String content) {
        MessageOutputDto messageOutputDto = new MessageOutputDto();
        messageOutputDto.setId(id);
        messageOutputDto.setContent(content);
        return messageOutputDto;
    }


}
