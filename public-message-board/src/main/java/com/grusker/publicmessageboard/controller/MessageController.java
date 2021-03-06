package com.grusker.publicmessageboard.controller;

import com.grusker.publicmessageboard.dto.MessageInputDto;
import com.grusker.publicmessageboard.dto.MessageOutputDto;
import com.grusker.publicmessageboard.exception.MessageNotFoundException;
import com.grusker.publicmessageboard.exception.UserNotAuthorizedException;
import com.grusker.publicmessageboard.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageOutputDto createMessage(@RequestBody MessageInputDto messageInputDto) {
        return messageService.createMessage(messageInputDto);
    }

    @PutMapping("/{id}")
    public MessageOutputDto updateMessage(@PathVariable Long id, @RequestBody MessageInputDto messageInputDto) throws MessageNotFoundException, UserNotAuthorizedException {
        return messageService.updateMessage(id, messageInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) throws MessageNotFoundException, UserNotAuthorizedException {
        messageService.deleteMessage(id);
    }

    @GetMapping
    public List<MessageOutputDto> getMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/{id}")
    public MessageOutputDto getMessage(@PathVariable Long id) {
        return messageService.getMessage(id);
    }
}