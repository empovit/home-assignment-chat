package com.github.empovit.roomchat.room;

import com.github.empovit.roomchat.conversations.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class MessageFormatter {

    public TextMessage format(Message message) {

        StringBuilder builder = new StringBuilder();
        builder.append("ID: ").append(message.getId()).append('\n')
                .append("TIME: ").append(message.getTime()).append('\n')
                .append("SENDER: ").append(message.getSender());

        String recipient = message.getRecipient();
        if (recipient != null) {
            builder.append("RECIPIENT: ").append(recipient).append('\n');
        }

        builder.append("TEXT: ").append(message.getText()).append('\n');
        return new TextMessage(builder);
    }
}
