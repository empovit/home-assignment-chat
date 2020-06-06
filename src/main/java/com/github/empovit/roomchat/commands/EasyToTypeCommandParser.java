package com.github.empovit.roomchat.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EasyToTypeCommandParser implements CommandParser {

    private static final String ALLOWED_PUNCTUATION = Pattern.quote("!\"#$%&'()*+,-.:;<=>?@[\\]^_`{|}~");

    // TODO: Allow slashes in the body by escaping with another slash (i.e. //)
    private static final Pattern COMMAND_PATTERN = Pattern.compile("/(\\w+) ([\\w\\s" + ALLOWED_PUNCTUATION + "]+)");

    @Override
    public Command parse(String text) {

        text = text.trim();

        // TODO: Return error if contains illegal elements between matching groups
        Matcher matcher = COMMAND_PATTERN.matcher(text);
        Map<String, String> attributes = new HashMap<>();

        String command = null;
        while (matcher.find()) {

            if (command == null) {
                command = matcher.group(1);
            }

            attributes.put(matcher.group(1), matcher.group(2).trim());
        }

        if (attributes.isEmpty()) {
            throw new IllegalArgumentException(String.format("Command is malformed: %s", text));
        }

        return new Command(command, attributes);
    }
}