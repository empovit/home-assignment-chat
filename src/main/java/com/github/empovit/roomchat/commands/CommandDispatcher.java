package com.github.empovit.roomchat.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandDispatcher {

    private final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);
    private final Map<String, CommandHandler> handlers = new ConcurrentHashMap<>(); // Can probably be thread-unsafe

    @Autowired
    private CommandParser parser;

    @Autowired
    public void registerHandlers(List<CommandHandler> wiredHandlers) {

        for (CommandHandler h : wiredHandlers) {
            handlers.put(h.getCommandName(), h);
        }

        logger.info("Registered handlers for commands: " + handlers.keySet());
    }

    public void dispatch(WebSocketSession session, String payload) throws IOException {

        Command command = parser.parse(payload);
        CommandHandler handler = handlers.get(command.getCommand());

        if (handler == null) {
            throw new IllegalArgumentException(String.format("Command not recognized: %s", command.getCommand()));
        }

        handler.handle(session, command);
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {

        for (CommandHandler handler : handlers.values()) {
            handler.disconnect(session, status);
        }
    }
}