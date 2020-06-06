package com.github.empovit.roomchat.conversations;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Message {
    private String id;
    private String user;
    private Set<String> attributes;
}
