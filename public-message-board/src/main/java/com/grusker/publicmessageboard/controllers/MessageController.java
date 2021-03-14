package com.grusker.publicmessageboard.controllers;

import com.grusker.publicmessageboard.dtos.MessageInputDto;
import com.grusker.publicmessageboard.dtos.MessageOutputDto;
import com.grusker.publicmessageboard.services.MessageService;
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
    public MessageOutputDto updateMessage(@PathVariable Long id, @RequestBody MessageInputDto messageInputDto) {
        return messageService.updateMessage(id, messageInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }

    @GetMapping
    public List<MessageOutputDto> getMessages() {
        return messageService.getMessages();
    }

}
