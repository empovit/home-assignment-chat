package com.github.empovit.roomchat.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
public class Command {
    private Map<String, String> attributes;
}
