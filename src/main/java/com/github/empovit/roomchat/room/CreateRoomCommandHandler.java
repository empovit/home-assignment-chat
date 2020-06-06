package com.github.empovit.roomchat.room;

import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import com.github.empovit.roomchat.user.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class CreateRoomCommandHandler implements CommandHandler {

    @Autowired
    private RoomRegistry rooms;

    @Autowired
    private UserRegistry users;

    @Override
    public String getCommandName() {
        return "room";
    }

    @Override
    public void handle(WebSocketSession session, Command command) {

        Map<String, String> attributes = command.getAttributes();
        String roomName = attributes.get(getCommandName());
        String userName = users.verifyUser(attributes);

        rooms.add(roomName);
        rooms.get(roomName).join(userName, session);
    }
}
