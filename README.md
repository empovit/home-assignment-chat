# Introduction

Simple room chat application based on WebSocket, with public room conversations and private direct messages.

# Building and Running

1. Start the server `./mvnw spring-boot:run` (or in remote debug mode 
   `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`)
2. Connect with a WebSocket client, for instance 
   [this](https://chrome.google.com/webstore/detail/websocket-test-client/fgponpodhbmadfljofbimhhlengambbn) 
   Chrome extension.
3. Start issuing commands and receiving messages.

# Supported commands

This chat uses simple text commands that are easy to type.

- /user _name_ - register a new user
- /room _name_ /as _user_ - create a chat room
- /join _name_ /as _user_ - join a chat room
- /chats _user_ - list user's conversations
- /messages _chat_ /as _user_ [/limit _number_] - show messages in a chat, optionally until some number        
- /pub _text_ /in _room_ /as _user_ - send a public message to a chat room
- /send _text_ /in _room_ /to _peer_ /as _user_ - send a direct message to another participant

Constraints:
- A chat room has only one public conversation.
- A chat room *always* has a public conversation.
- A user can send and receive messages on a private conversation only if she is one of the two participants.

Nice to have:
- A user can leave a chat room.
- A user can see who has joined the room.



  
