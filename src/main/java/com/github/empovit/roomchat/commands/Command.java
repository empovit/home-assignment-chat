package com.github.empovit.roomchat.commands;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Command {
    private final String command;
    private final Map<String, String> attributes;
}
