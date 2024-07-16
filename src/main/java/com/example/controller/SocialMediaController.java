package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

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

    private boolean newMessageTextIsValid(String messageText) {
        if (messageText.length() == 0) {return false;}
        if (messageText.length() >= 255) {return false;}
        return true;
    }

    private boolean newMessageIsValid(Message message) {
        if (!newMessageTextIsValid(message.getMessageText())) {return false;}
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

}
