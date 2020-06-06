package com.github.empovit.roomchat;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public interface ChatDispatcher {

    void dispatch(WebSocketSession session, String payload);

    void disconnect(WebSocketSession session, CloseStatus status);
}
