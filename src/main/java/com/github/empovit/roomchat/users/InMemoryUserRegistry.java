package com.github.empovit.roomchat.users;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryUserRegistry implements UserRegistry {

    private final Set<String> users = new HashSet<>();

    @Override
    public synchronized void verifyUser(String userName) {

        if (userName == null || userName.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        if (!users.contains(userName)) {
            throw new IllegalArgumentException("Unknown user: " + userName);
        }
    }

    @Override
    public synchronized void addUser(String userName) {

        if (userName == null || userName.length() == 0) {
            throw new IllegalArgumentException("User name must be specified");
        }

        if (users.contains(userName)) {
            throw new IllegalArgumentException("User already exists: " + userName);
        }

        users.add(userName);
    }
}
