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
    private final Map<String, Map<String, WebSocketSession>> connections = new ConcurrentHashMap<>();

    @Autowired
    private ConversationHistory history;

    @Autowired
    private MessageFormatter formatter;

    public void join(String user, WebSocketSession session) {

        connections.compute(user, (key, value) -> {

            if (value == null) {
                value = Collections.synchronizedMap(new HashMap<>());
            }

            value.put(session.getId(), session);
            return value;
        });
    }

    public void publish(String userName, String text) {

        if (!connections.containsKey(userName)) {
            throw new IllegalArgumentException("User is not a member of the room: " + userName);
        }

        Message message = new Message(UUID.randomUUID().toString(), System.currentTimeMillis(), userName, null, text);

        history.publish(message);

        WebSocketMessage<String> wsMessage = formatter.format(message);

        for (Map.Entry<String, Map<String, WebSocketSession>> conn : connections.entrySet()) {
            // TODO: Do we need to prevent "echo"? if (!conn.getKey().equals(userName))...

            Iterator<Map.Entry<String, WebSocketSession>> iterator = conn.getValue().entrySet().iterator();
            while (iterator.hasNext()) {

                WebSocketSession session = iterator.next().getValue();

                try {
                    session.sendMessage(wsMessage);
                } catch (IOException e) {

                    if (!session.isOpen()) {
                        iterator.remove();
                    }

                    // log error and keep trying for other users
                    logger.error("Failed to send message to " + conn.getKey(), e);
                }
            }
        }
    }
}
