package com.github.empovit.roomchat.room;

import com.github.empovit.roomchat.conversations.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

@Component
public class MessageFormatter {

    public WebSocketMessage<?> format(Message message) {

        StringBuilder builder = new StringBuilder("--");
        builder.append("ID:\t").append(message.getId()).append('\n')
                .append("TIME:\t").append(message.getTime()).append('\n')
                .append("ROOM:\t").append(message.getRoom()).append('\n')
                .append("FROM:\t").append(message.getSender()).append('\n');

        String recipient = message.getRecipient();
        if (recipient != null) {
            builder.append("TO:\t").append(recipient).append('\n');
        }

        builder.append("TEXT:\t").append(message.getText()).append("\n");
        return new TextMessage(builder);
    }

    public WebSocketMessage<?> error(Exception e) {
        return new TextMessage(String.format("--\nERROR:\t%s\n", e.getMessage()));
    }
}