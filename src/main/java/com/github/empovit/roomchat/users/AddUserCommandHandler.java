package com.github.empovit.roomchat.users;

import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class AddUserCommandHandler implements CommandHandler {

    @Autowired
    private UserRegistry users;

    @Override
    public String getCommandName() {
        return "user";
    }

    @Override
    public void handle(WebSocketSession session, Command command) {
        Map<String, String> attributes = command.getAttributes();
        String userName = attributes.get(getCommandName());
        users.addUser(userName);
    }
}