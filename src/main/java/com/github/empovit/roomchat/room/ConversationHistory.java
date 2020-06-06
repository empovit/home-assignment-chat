package com.github.empovit.roomchat.room;

import com.github.empovit.roomchat.conversations.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("prototype")
public class ConversationHistory {

    private final List<Message> messages = Collections.synchronizedList(new LinkedList<>());

    public void publish(Message message) {
        messages.add(message);
    }
}
