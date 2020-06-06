package com.github.empovit.roomchat.commands;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface CommandHandler {

    String getCommandName();

    void handle(WebSocketSession session, Command command) throws IOException;

    default void disconnect(WebSocketSession session, CloseStatus status) {
    }
}
