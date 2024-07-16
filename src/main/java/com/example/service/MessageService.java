package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Message addMessage(Message inputMessage) {
        return messageRepository.save(inputMessage);
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<Message>();
        Iterable<Message> iterable = messageRepository.findAll();
        for (Message message : iterable) {
            messages.add(message);
        }
        return messages;
    }

    public Message findById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public Integer deleteById(Integer messageId) {
        int rowsAffected = 0;
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            rowsAffected = 1;
            messageRepository.deleteById(messageId);
        }
        return rowsAffected;
    }
}
