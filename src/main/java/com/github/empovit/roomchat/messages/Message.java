package com.github.empovit.roomchat.messages;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class Message {
    private final String id;
    private final long time;
    private final String room;
    private final String sender;
    private final String recipient;
    private final String text;
}
