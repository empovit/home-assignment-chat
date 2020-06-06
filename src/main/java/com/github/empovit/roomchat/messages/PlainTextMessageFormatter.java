package com.github.empovit.roomchat.messages;

import com.github.empovit.roomchat.conversations.ConversationMeta;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import java.util.Objects;

@Component
public class PlainTextMessageFormatter implements MessageFormatter {

    @Override
    public WebSocketMessage<?> format(Message message) {

        Objects.requireNonNull(message, "Message is required");

        StringBuilder builder = new StringBuilder("--\n")
                .append("ID:\t").append(message.getId()).append('\n')
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

    @Override
    public WebSocketMessage<?> error(Exception e) {
        Objects.requireNonNull(e, "Exception is required");
        return new TextMessage(String.format("--\nERROR:\t%s\n", e.getMessage()));
    }

    @Override
    public WebSocketMessage<?> format(ConversationMeta meta) {

        Objects.requireNonNull(meta, "Conversation metadata is required");

        StringBuilder builder = new StringBuilder("--\n")
                .append("ID:\t").append(meta.getId()).append('\n')
                .append("ABOUT:\t").append(meta.getDescription()).append('\n');
        return new TextMessage(builder);
    }
}