package com.github.empovit.roomchat.room;

import com.github.empovit.roomchat.conversations.Message;
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
public class Room {

    private final Logger logger = LoggerFactory.getLogger(Room.class);

    // Keep track of session IDs to prevent joining multiple times with the same session
    private final Map<String, Map<String, WebSocketSession>> connectionsByUser = new ConcurrentHashMap<>();

    private final String name;

    @Autowired
    private ConversationHistory history;

    @Autowired
    private MessageFormatter formatter;

    public Room(String name) {
        this.name = name;
    }

    public void join(String user, WebSocketSession session) {

        connectionsByUser.compute(user, (key, value) -> {

            if (value == null) {
                value = Collections.synchronizedMap(new HashMap<>());
            }

            value.put(session.getId(), session);
            return value;
        });
    }

    public void publish(String userName, String text) {

        if (!connectionsByUser.containsKey(userName)) {
            throw new IllegalArgumentException("User is not a member of the room: " + userName);
        }

        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .time(System.currentTimeMillis())
                .room(this.name)
                .sender(userName)
                .text(text).build();

        history.publish(message); // write to the history first thing
        WebSocketMessage<?> wsMessage = formatter.format(message);
        for (Map.Entry<String, Map<String, WebSocketSession>> conn : connectionsByUser.entrySet()) {

            if (conn.getKey().equals(userName)) {
                // TODO: Do we need to "echo" the messages to the sender?
                continue;
            }

            deliverMessage(conn.getKey(), wsMessage, conn.getValue());
        }
    }

    public void send(String sender, String recipient, String text) {

        if (!connectionsByUser.containsKey(sender)) {
            throw new IllegalArgumentException("User is not a member of the room: " + sender);
        }

        Map<String, WebSocketSession> recipientSessions = connectionsByUser.get(recipient);
        if (recipientSessions == null) {
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

        WebSocketMessage<?> wsMessage = formatter.format(message);
        // TODO: Do we need to "echo" the messages to the sender?
        deliverMessage(recipient, wsMessage, recipientSessions);
    }


    private void deliverMessage(String recipient, WebSocketMessage<?> wsMessage,
                                Map<String, WebSocketSession> destinations) {

        Iterator<Map.Entry<String, WebSocketSession>> iterator = destinations.entrySet().iterator();
        while (iterator.hasNext()) {

            WebSocketSession session = iterator.next().getValue();

            try {
                session.sendMessage(wsMessage);
            } catch (IOException e) {

                if (!session.isOpen()) {
                    iterator.remove();
                }

                // log error and keep trying for other users
                logger.error("Failed to send message to " + recipient, e);
            }
        }
    }

    public void disconnect(WebSocketSession session) {

        // TODO: Can be optimized, for now traverse all users
        // We do not know whom the session belonged to. Also one client can join with multiple user names.
        String id = session.getId();
        for (Map<String, WebSocketSession> sessions : connectionsByUser.values()) {
            sessions.remove(id);
        }
    }
}