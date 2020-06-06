package com.github.empovit.roomchat.conversations;

import com.github.empovit.roomchat.messages.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Conversation {

    private final ConversationMeta meta;

    private final List<Message> messages = Collections.synchronizedList(new LinkedList<>());

    public void add(Message message) {
        messages.add(message);
    }

    public Collection<Message> list() {
        return Collections.unmodifiableList(messages);
    }
}
