package com.github.empovit.roomchat.commands;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public interface CommandHandler {

    String getCommandName();

    void handle(WebSocketSession session, Command command);

    default void disconnect(WebSocketSession session, CloseStatus status) {
    }
}
