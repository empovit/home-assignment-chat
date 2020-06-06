package com.github.empovit.roomchat;

import com.github.empovit.roomchat.commands.CommandDispatcher;
import com.github.empovit.roomchat.messages.MessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    @Autowired
    private CommandDispatcher dispatcher;

    @Autowired
    private MessageFormatter formatter;

    @Value("${session.send.time.limit:2000}")
    private int sendTimeLimit;

    @Value("${session.buffer.size.limit:1000}")
    private int bufferSizeLimit;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        WebSocketSession concurrentSession =
                new ConcurrentWebSocketSessionDecorator(session, sendTimeLimit, bufferSizeLimit);

        try {

            dispatcher.dispatch(concurrentSession, message.getPayload());

        } catch (NullPointerException e) {
            logger.error("NullPointerException ", e);
            concurrentSession.sendMessage(formatter.error(
                    new IllegalArgumentException("This is embarrassing, we've got a NPE :(")));
        } catch (IllegalArgumentException e) {
            logger.warn("User error: " + e.getMessage());
            concurrentSession.sendMessage(formatter.error(e));
        } catch (Exception e) {
            logger.error("Failed to dispatch a command", e);
            concurrentSession.sendMessage(formatter.error(e));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        dispatcher.disconnect(session, status);
    }
}