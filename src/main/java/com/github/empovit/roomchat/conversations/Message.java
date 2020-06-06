package com.github.empovit.roomchat.conversations;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String id;
    private long time;
    private String sender;
    private String recipient;
    private String text;
}
