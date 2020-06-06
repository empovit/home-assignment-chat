package com.github.empovit.roomchat.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserRegistry {

    private static final String USER_ATTRIBUTE = "as";

    private final Set<String> users = new HashSet<>();

    public String extractUser(Map<String, String> attributes) {

        String userName = attributes.get(USER_ATTRIBUTE);
        if (userName == null) {
            throw new IllegalArgumentException(
                    String.format("User name must be specified using '%s' key", USER_ATTRIBUTE));
        }

        return userName;
    }

    public synchronized String getUser(String userName) {

        if (users.contains(userName)) {
            return userName;
        }

        throw new IllegalArgumentException("Unknown user: " + userName);
    }

    public synchronized void addUser(String userName) {

        if (userName == null || userName.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        if (users.contains(userName)) {
            throw new IllegalArgumentException("User already exists: " + userName);
        }

        users.add(userName);
    }

    public String verifyUser(Map<String, String> attributes) {
        return getUser(extractUser(attributes));
    }
}
