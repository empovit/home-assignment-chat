package com.github.empovit.roomchat;

import com.github.empovit.roomchat.commands.Command;
import com.github.empovit.roomchat.commands.CommandHandler;
import com.github.empovit.roomchat.commands.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomChatDispatcher implements ChatDispatcher {

    private final Logger logger = LoggerFactory.getLogger(RoomChatDispatcher.class);

    @Autowired
    private CommandParser parser;

    private Map<String, CommandHandler> handlers = new ConcurrentHashMap<>();

    @Autowired
    public void registerHandlers(List<CommandHandler> wiredHandlers) {

        for (CommandHandler h : wiredHandlers) {
            handlers.put(h.getCommandName(), h);
        }

        logger.info("Registered handlers for commands: " + handlers.keySet());
    }

    @Override
    public void dispatch(WebSocketSession session, String payload) {

        Command command = parser.parse(payload);
        CommandHandler handler = handlers.get(command.getCommand());

        if (handler == null) {
            throw new UnsupportedOperationException(String.format("Unsupported command: %s", command.getCommand()));
        }

        handler.handle(session, command);
    }
}
