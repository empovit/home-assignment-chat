package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@Scope("prototype")
public class InMemoryRoomHistory implements RoomHistory {

    private final String name;

    private final Conversation history;

    public InMemoryRoomHistory(String name) {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        this.name = name;
        ConversationMeta meta = new ConversationMeta(
                UUID.randomUUID().toString(),
                String.format("Public in room \"%s\"", name));
        this.history = new Conversation(meta);
    }

    @Override
    public void add(Message message) {
        history.add(Objects.requireNonNull(message, "Message is required"));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Conversation getConversation() {
        return history;
    }
}
