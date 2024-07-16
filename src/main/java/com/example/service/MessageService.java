package com.example.service;

import java.util.ArrayList;
import java.util.List;

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
}
