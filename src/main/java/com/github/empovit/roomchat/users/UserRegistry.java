package com.github.empovit.roomchat.users;

public interface UserRegistry {

    void verifyUser(String userName);

    void addUser(String userName);
}
