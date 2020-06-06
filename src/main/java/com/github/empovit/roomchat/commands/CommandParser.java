package com.github.empovit.roomchat.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    public Map<String, String> parse(String text) {

        text = text.trim();

        // TODO: Allow slashes by using double-slash for escaping
        Pattern pattern = Pattern.compile("/(\\w+) ([\\w\\s"
                + Pattern.quote("!\"#$%&'()*+,-.:;<=>?@[\\]^_`{|}~") + "]+)");

        Matcher matcher = pattern.matcher(text);
        Map<String, String> attributes = new HashMap<>();

        // TODO: Error is contains illegal elements between matching groups
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2));
        }

        if (attributes.isEmpty()) {
            throw new IllegalArgumentException(String.format("Malformed command: %s", text));
        }

        return attributes;
    }
}
