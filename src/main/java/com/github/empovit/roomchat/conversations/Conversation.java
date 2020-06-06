package com.github.empovit.roomchat.conversations;

import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.LinkedList;
import java.util.List;

public class Conversation {

    private final List<Message> history = new LinkedList<>();

    public void registerReceivers(List<WebSocketSession> sessions) {

    }

    public void registerReceiver(WebSocketSession session) {
        //TODO: Push the history
    }

    public void registerSender(WebSocketSession session) {

    }
}
