package com.github.empovit.roomchat.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoomRegistry {

    private static final Map<String, RoomBroker> rooms = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    public synchronized void add(String name) {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        if (rooms.containsKey(name)) {
            throw new IllegalArgumentException("Room already exists: " + name);
        }

        rooms.put(name, context.getBean(RoomBroker.class, name));
    }

    public synchronized RoomBroker get(String name) {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        RoomBroker room = rooms.get(name);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + name);
        }

        return room;
    }

    public synchronized void removeSession(WebSocketSession session) {

        // TODO: Can be optimized by tracking all rooms for a session
        for (RoomBroker r : rooms.values()) {
            r.disconnect(session);
        }
    }
}
