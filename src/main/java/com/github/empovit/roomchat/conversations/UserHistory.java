package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;

import java.util.Collection;

public interface UserHistory {

    void addRoom(RoomHistory room);

    void addIncoming(Message message);

    void addOutgoing(Message message);

    Collection<ConversationMeta> getConversations();

    Conversation getConversation(String id);
}
