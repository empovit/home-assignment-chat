package com.github.empovit.roomchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = WebMvcAutoConfiguration.class)
public class RoomChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomChatApplication.class, args);
    }
}
