package com.example.clientServer.controller;

import com.example.clientServer.model.ChatMessage;
import com.example.clientServer.model.User;
import com.example.clientServer.repository.ChatMessageRepository;
import com.example.clientServer.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Tan Dang
 * @since 22/09/2023 - 9:17 pm
 */
@Controller
@RestController
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatController(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage
            , SimpMessageHeaderAccessor headerAccessor) {
        try {
            if (headerAccessor.getSessionAttributes() != null) {
                String username = (String) headerAccessor.getSessionAttributes().get("username");
                chatMessage.setSender(username);
                chatMessageRepository.save(chatMessage);
                User user = userRepository.findByUsername(chatMessage.getSender());
                if (user == null) {
                    user = new User();
                    user.setUsername(chatMessage.getSender());
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("No information Username");
        }
        return chatMessage;
    }
    @GetMapping("/messages")
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
        if (attributes != null) {
            attributes.put("username", chatMessage.getSender());
        }
        return chatMessage;
    }
}
