package com.github.empovit.roomchat.communication;

import com.github.empovit.roomchat.commands.CallerIdentity;
import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import com.github.empovit.roomchat.rooms.RoomRegistry;
import com.github.empovit.roomchat.users.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class PublishCommandHandler implements CommandHandler {

    @Autowired
    private RoomRegistry rooms;

    @Autowired
    private UserRegistry users;

    @Autowired
    private CallerIdentity identity;

    @Override
    public String getCommandName() {
        return "pub";
    }

    @Override
    public void handle(WebSocketSession session, Command command) {

        Map<String, String> attributes = command.getAttributes();
        String text = attributes.get(getCommandName());

        String roomName = attributes.get("in");

        String userName = identity.getCaller(attributes);
        users.verifyUser(userName);

        rooms.get(roomName).publish(userName, text);
    }
}