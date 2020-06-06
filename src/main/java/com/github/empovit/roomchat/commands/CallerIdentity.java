package com.github.empovit.roomchat.commands;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CallerIdentity {

    private static final String USER_ATTRIBUTE = "as";

    public String getCaller(Map<String, String> attributes) {

        String userName = attributes.get(USER_ATTRIBUTE);
        if (userName == null) {
            throw new IllegalArgumentException(
                    String.format("User name must be specified via the '%s' key", USER_ATTRIBUTE));
        }

        return userName;
    }
}
