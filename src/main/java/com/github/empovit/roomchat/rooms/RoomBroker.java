package com.github.empovit.roomchat.rooms;

import com.github.empovit.roomchat.conversations.ConversationHistory;
import com.github.empovit.roomchat.messages.Message;
import com.github.empovit.roomchat.messages.MessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class RoomBroker {

    private final Map<String, FanOut> connectionsByUser = new ConcurrentHashMap<>();

    private final String name;

    @Autowired
    private ConversationHistory history;

    @Autowired
    private MessageFormatter formatter;

    public RoomBroker(String name) {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        this.name = name;
    }

    public void join(String user, WebSocketSession session) {

        Objects.requireNonNull(session, "Session is required");

        if (user == null || user.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        history.addRoom(this.name, user);
        FanOut fanout = connectionsByUser.computeIfAbsent(user, FanOut::new);
        fanout.add(session);
    }

    public void publish(String userName, String text) {

        if (userName == null || userName.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("Non-empty message text is required");
        }

        if (!connectionsByUser.containsKey(userName)) {
            throw new IllegalArgumentException("User is not a member of the room: " + userName);
        }

        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .time(System.currentTimeMillis())
                .room(this.name)
                .sender(userName)
                .text(text).build();

        history.publish(message); // write to the history, first thing
        WebSocketMessage<?> wsMessage = formatter.format(message);
        for (Map.Entry<String, FanOut> fanOuts : connectionsByUser.entrySet()) {

            if (fanOuts.getKey().equals(userName)) {
                // TODO: Do we need to "echo" the messages to the sender?
                continue;
            }

            fanOuts.getValue().deliver(wsMessage);
        }
    }

    public void send(String sender, String recipient, String text) {

        if (sender == null || sender.length() == 0) {
            throw new IllegalArgumentException("Sender must be specified");
        }

        if (recipient == null || recipient.length() == 0) {
            throw new IllegalArgumentException("Recipient must be specified");
        }

        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("Non-empty message text is required");
        }

        if (!connectionsByUser.containsKey(sender)) {
            throw new IllegalArgumentException("User is not a member of the room: " + sender);
        }

        FanOut recipientFanOut = connectionsByUser.get(recipient);
        if (recipientFanOut == null) {
            throw new IllegalArgumentException("User is not a member of the room: " + recipient);
        }

        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .time(System.currentTimeMillis())
                .room(this.name)
                .sender(sender)
                .recipient(recipient)
                .text(text).build();

        history.send(message);

        // TODO: Do we need to "echo" the messages to the sender?
        recipientFanOut.deliver(formatter.format(message));
    }


    public void disconnect(WebSocketSession session) {

        // TODO: Can be optimized, for now traverse all users
        // We do not know whom the session belonged to. Also one client can join with multiple user names.
        String id = session.getId();
        for (FanOut fanOut : connectionsByUser.values()) {
            fanOut.remove(id);
        }
    }

    private static class FanOut {

        private final Logger logger = LoggerFactory.getLogger(FanOut.class);

        // Keep track of session IDs to prevent joining multiple times with the same session
        private final Map<String, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

        private final String owner;

        FanOut(String owner) {
            this.owner = owner;
        }

        public void add(WebSocketSession session) {
            sessions.put(session.getId(), session);
        }

        public void remove(String id) {
            sessions.remove(id);
        }

        public void deliver(WebSocketMessage<?> wsMessage) {

            for (Map.Entry<String, WebSocketSession> stringWebSocketSessionEntry : sessions.entrySet()) {

                WebSocketSession session = stringWebSocketSessionEntry.getValue();

                try {
                    session.sendMessage(wsMessage);
                } catch (IOException e) {
                    // log error and keep trying on other connections
                    logger.error("Failed to send message to " + owner, e);
                }
            }
        }
    }
}