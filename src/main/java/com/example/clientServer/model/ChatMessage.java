package com.example.clientServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tan Dang
 * @since 22/09/2023 - 9:03 pm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType{
        CHAT, JOIN, LEAVE
    }
}

