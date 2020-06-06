package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConversationHistory {

    private final Map<String, RoomHistory> rooms = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, UserHistory> users = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private ApplicationContext context;

    public void addRoom(String room, String user) {

        if (user == null || user.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        if (room == null || room.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        UserHistory userHistory = users.computeIfAbsent(user, userName -> context.getBean(UserHistory.class));
        RoomHistory roomHistory = rooms.computeIfAbsent(room, roomName -> context.getBean(RoomHistory.class, roomName));
        userHistory.addRoom(roomHistory);
    }

    public void publish(Message message) {
        String room = Objects.requireNonNull(message, "Message is required").getRoom();
        RoomHistory history = rooms.computeIfAbsent(room, key -> context.getBean(RoomHistory.class));
        history.add(message);
    }

    public void send(Message message) {
        Objects.requireNonNull(message, "Message is required");
        users.get(message.getSender()).addOutgoing(message);
        users.get(message.getRecipient()).addIncoming(message);
    }

    public Collection<ConversationMeta> getConversations(String user) {

        if (user == null || user.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        UserHistory userHistory = users.get(user);
        return userHistory == null ? Collections.emptyList() : userHistory.getConversations();
    }

    public Conversation getConversation(String user, String conversationId) {

        if (user == null || user.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        UserHistory userHistory = users.get(user);
        if (userHistory == null) {
            throw new IllegalArgumentException("User does not have conversation history");
        }

        Conversation conversation = userHistory.getConversation(conversationId);
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }

        return conversation;
    }
}
