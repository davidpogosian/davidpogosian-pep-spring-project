package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    // Misc.

    private boolean newAccountIsValid(Account account) {
        if (account.getUsername().equals("")) {return false;}
        if (account.getPassword().length() < 4) {return false;}
        return true;
    }

    private boolean messageTextIsValid(String messageText) {
        if (messageText.length() == 0) {return false;}
        if (messageText.length() >= 255) {return false;}
        return true;
    }

    private boolean newMessageIsValid(Message message) {
        if (!messageTextIsValid(message.getMessageText())) {return false;}
        if (accountService.findById(message.getPostedBy()) == null) {return false;}
        return true;
    }

    // Handle requests.

    @PostMapping("/register")
    public ResponseEntity<Account> handleRegister(@RequestBody Account inputAccount) {
        if (!newAccountIsValid(inputAccount)) {
            return ResponseEntity.status(400).body(null);
        }

        if (accountService.findByUsername(inputAccount.getUsername()) != null) {
            return ResponseEntity.status(409).body(null);
        }

        Account newAccount = accountService.addAccount(inputAccount);
        return ResponseEntity.status(200).body(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> handleLogin(@RequestBody Account inputAccount) {
        Account account = accountService.findByUsername(inputAccount.getUsername());
        
        if (account == null) {
            return ResponseEntity.status(401).body(null);
        }

        if (!account.getPassword().equals(inputAccount.getPassword())) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(200).body(account);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> handleCreateNewMessage(@RequestBody Message inputMessage) {
        if (!newMessageIsValid(inputMessage)) {
            return ResponseEntity.status(400).body(null);
        }

        Message message = messageService.addMessage(inputMessage);
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> handleGetAllMessages() {
        List<Message> messages = messageService.getAll();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> handleGetMessageById(@PathVariable("message_id") Integer messageId) {
        return ResponseEntity.status(200).body(messageService.findById(messageId));
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> handleDeleteMessageById(@PathVariable("message_id") Integer messageId) {
        Integer rowsAffected = messageService.deleteById(messageId);
        if (rowsAffected == 1) {
            return ResponseEntity.status(200).body(rowsAffected);
        }
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> handleUpdateMessageById(@PathVariable("message_id") Integer messageId, @RequestBody Message inputMessage) {
        if (messageService.findById(messageId) == null) {
            return ResponseEntity.status(400).build();
        }

        if (!messageTextIsValid(inputMessage.getMessageText())) {
            return ResponseEntity.status(400).build();
        }

        messageService.updateById(messageId, inputMessage.getMessageText());
        return ResponseEntity.status(200).body(1);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> handleGetMessagesByAccountId(@PathVariable("account_id") Integer accountId) {
        List<Message> messages = messageService.findAllByAccountId(accountId);
        return ResponseEntity.status(200).body(messages);
    }
}
