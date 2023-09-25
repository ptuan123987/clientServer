package com.example.clientServer;

import com.example.clientServer.model.ChatMessage;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

/**
 * @author Tan Dang
 * @since 22/09/2023 - 9:05 pm
 */
@Component
@Log
public class WebSocketEventListener {
    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Got client");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String username = getUsername(event);

        if (username != null) {
            log.info("Users disconnected: " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        } else {
            log.info("someone left");
        }
    }

    private String getUsername(AbstractSubProtocolEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
        if (attributes != null) {
            return (String) attributes.get("username");
        }
        return null;
    }
}
