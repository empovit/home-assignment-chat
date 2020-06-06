package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.commands.CallerIdentity;
import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import com.github.empovit.roomchat.messages.Message;
import com.github.empovit.roomchat.messages.MessageFormatter;
import com.github.empovit.roomchat.users.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
public class ReadMessagesCommandHandler implements CommandHandler {

    @Autowired
    private MessageFormatter formatter;

    @Autowired
    private UserRegistry users;

    @Autowired
    private CallerIdentity identity;

    @Autowired
    private ConversationHistory history;

    @Override
    public String getCommandName() {
        return "messages";
    }

    @Override
    public void handle(WebSocketSession session, Command command) throws IOException {

        Map<String, String> attributes = command.getAttributes();

        String caller = identity.getCaller(attributes);
        users.verifyUser(caller);

        String conversationId = attributes.get(getCommandName());
        Conversation conversation = history.getConversation(caller, conversationId);

        for (Message m : conversation.getMessages()) {
            session.sendMessage(formatter.format(m));
        }
    }
}