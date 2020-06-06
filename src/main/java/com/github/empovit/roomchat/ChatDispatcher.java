package com.github.empovit.roomchat;

import org.springframework.web.socket.WebSocketSession;

public interface ChatDispatcher {

    void dispatch(WebSocketSession session, String payload);
}
