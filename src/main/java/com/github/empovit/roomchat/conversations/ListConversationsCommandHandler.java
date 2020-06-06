package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import com.github.empovit.roomchat.messages.MessageFormatter;
import com.github.empovit.roomchat.users.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Component
public class ListConversationsCommandHandler implements CommandHandler {

    @Autowired
    private MessageFormatter formatter;

    @Autowired
    private UserRegistry users;

    @Autowired
    private ConversationHistory history;

    @Override
    public String getCommandName() {
        return "chats";
    }

    @Override
    public void handle(WebSocketSession session, Command command) throws IOException {

        Map<String, String> attributes = command.getAttributes();

        String user = attributes.get(getCommandName());
        users.verifyUser(user);

        Collection<ConversationMeta> conversations = history.getConversations(user);
        for (ConversationMeta c : conversations) {
            session.sendMessage(formatter.format(c));
        }
    }
}