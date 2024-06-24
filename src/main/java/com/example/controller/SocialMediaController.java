package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
@RequestMapping("/api")
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
                account.getPassword() == null || account.getPassword().length() < 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Account> registeredAccount = accountService.registerAccount(account);
        return registeredAccount.map(acc -> new ResponseEntity<>(acc, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        return loggedInAccount.map(acc -> new ResponseEntity<>(acc, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
                message.getMessageText().length() > 255 || message.getPostedBy() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Message> createdMessage = messageService.createMessage(message);
        return createdMessage.map(msg -> new ResponseEntity<>(msg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.map(msg -> new ResponseEntity<>(msg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        return new ResponseEntity<>(deleted ? "1" : "", HttpStatus.OK);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
                message.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Message> updatedMessage = messageService.updateMessage(messageId, message.getMessageText());
        return updatedMessage.map(msg -> new ResponseEntity<>("1", HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
