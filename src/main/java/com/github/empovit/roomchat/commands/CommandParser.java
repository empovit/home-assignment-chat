package com.github.empovit.roomchat.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommandParser {

    public Command parse(String text) {

        text = text.trim();

        // TODO: Allow slashes by using double-slash for escaping
        Pattern pattern = Pattern.compile("/(\\w+) ([\\w\\s"
                + Pattern.quote("!\"#$%&'()*+,-.:;<=>?@[\\]^_`{|}~")
                + "]+)");

        Matcher matcher = pattern.matcher(text);
        Map<String, String> attributes = new HashMap<>();

        // TODO: Error is contains illegal elements between matching groups
        String command = null;

        while (matcher.find()) {

            if (command == null) {
                command = matcher.group(1);
            }

            attributes.put(matcher.group(1), matcher.group(2));
        }

        if (attributes.isEmpty()) {
            throw new IllegalArgumentException(String.format("Malformed command: %s", text));
        }

        return new Command(command, attributes);
    }
}
