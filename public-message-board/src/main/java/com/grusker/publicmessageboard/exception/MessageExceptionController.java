package com.grusker.publicmessageboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MessageExceptionController {

    @ExceptionHandler(value = MessageNotFoundException.class)
    public ResponseEntity<Object> exception(MessageNotFoundException ex) {
        return new ResponseEntity<>("Message not found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserNotAuthorizedException.class)
    public ResponseEntity<Object> exception(UserNotAuthorizedException ex) {
        return new ResponseEntity<>("User is not authorized to do the operation.", HttpStatus.CONFLICT);
    }
}