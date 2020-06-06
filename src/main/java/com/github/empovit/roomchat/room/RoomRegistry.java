package com.github.empovit.roomchat.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoomRegistry {

    @Autowired
    private ApplicationContext context;

    private static final Map<String, Room> rooms = new HashMap<>();

    public synchronized void add(String name) {

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Room name must be specified");
        }

        if (rooms.containsKey(name)) {
            throw new IllegalArgumentException("Room already exists: " + name);
        }

        rooms.put(name, context.getBean(Room.class));
    }

    public synchronized Room get(String name) {

        Room room = rooms.get(name);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + name);
        }

        return room;
    }
}
