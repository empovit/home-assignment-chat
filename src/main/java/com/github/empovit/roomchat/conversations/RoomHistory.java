package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;

public interface RoomHistory {

    void add(Message message);

    String getName();

    Conversation getConversation();
}
