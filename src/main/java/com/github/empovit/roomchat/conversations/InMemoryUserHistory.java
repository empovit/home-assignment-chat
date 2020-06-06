package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("prototype")
public class InMemoryUserHistory implements UserHistory {

    private final Map<String, Conversation> historyByPeer = Collections.synchronizedMap(new HashMap<>());
    private final Map<ConversationMeta, Conversation> history = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void addRoom(RoomHistory room) {
        Conversation conversation = Objects.requireNonNull(room, "Room is required").getConversation();
        history.putIfAbsent(conversation.getMeta(), conversation);
    }

    @Override
    public void addIncoming(Message message) {
        Objects.requireNonNull(message, "Message is required");
        add(message.getSender(), message);
    }

    @Override
    public void addOutgoing(Message message) {
        Objects.requireNonNull(message, "Message is required");
        add(message.getRecipient(), message);
    }

    private void add(String who, Message message) {

        if (who == null || who.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        Objects.requireNonNull(message, "Message is required");

        Conversation conversation = historyByPeer.computeIfAbsent(who, user -> {
            ConversationMeta meta = new ConversationMeta(
                    UUID.randomUUID().toString(),
                    String.format("Private with user \"%s\"", user));
            return new Conversation(meta);
        });

        conversation.add(message);
        history.putIfAbsent(conversation.getMeta(), conversation);
    }

    @Override
    public Collection<ConversationMeta> getConversations() {
        return history.keySet();
    }

    @Override
    public Conversation getConversation(String id) {
        return history.get(new ConversationMeta(id));
    }
}