package com.github.empovit.roomchat.conversations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String id;
    private long time;
    private String room;
    private String sender;
    private String recipient;
    private String text;
}
