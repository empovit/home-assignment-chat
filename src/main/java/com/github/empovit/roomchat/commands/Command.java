package com.github.empovit.roomchat.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Command {
    private final String command;
    private final Map<String, String> attributes;
}
