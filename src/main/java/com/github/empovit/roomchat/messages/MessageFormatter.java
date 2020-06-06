package com.github.empovit.roomchat.messages;

import com.github.empovit.roomchat.conversations.ConversationMeta;
import org.springframework.web.socket.WebSocketMessage;

public interface MessageFormatter {

    WebSocketMessage<?> format(Message message);

    WebSocketMessage<?> error(Exception e);

    WebSocketMessage<?> format(ConversationMeta meta);
}
