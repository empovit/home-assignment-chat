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
public class DirectMessageCommandHandler implements CommandHandler {

    @Autowired
    private RoomRegistry rooms;

    @Autowired
    private UserRegistry users;

    @Autowired
    private CallerIdentity identity;

    @Override
    public String getCommandName() {
        return "send";
    }

    @Override
    public void handle(WebSocketSession session, Command command) {

        Map<String, String> attributes = command.getAttributes();

        String text = attributes.get(getCommandName());
        String roomName = attributes.get("in");

        String userName = identity.getCaller(attributes);
        users.verifyUser(userName);

        String recipient = attributes.get("to");
        rooms.get(roomName).send(userName, recipient, text);
    }
}