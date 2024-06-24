package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Message> createMessage(Message message) {
        if (accountRepository.existsById(message.getPostedBy())) {
            Message savedMessage = messageRepository.save(message);
            return Optional.of(savedMessage);
        }
        return Optional.empty();
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public Optional<Message> updateMessage(Integer messageId, String messageText) {
        return messageRepository.findById(messageId)
                .map(message -> {
                    message.setMessageText(messageText);
                    return messageRepository.save(message);
                });
    }

    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
