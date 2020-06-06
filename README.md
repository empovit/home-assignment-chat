# Introduction

Simple room chat application based on WebSocket, with public room conversations and private direct messages.

# Running 

# Supported commands

This chat uses simple text commands that are easy to type.

| Command                               | Description           |
|---------------------------------------|-----------------------|
|/user \<name\>                         | register a new user   |
|/room \<name\> /as \<user\>            | create a chat room    |
|/join \<name\> /as \<user\>            | join a chat room      |

3. Leave a chat room: `/leave <name> /as <user>`
4. Read all conversations visible to the user: 
2. Read all messages in a conversation. 
3. Send a message to the public conversation of a chat room.
4. Initiate a private conversation with another user.
5. Send a direct message to the other participant in a private conversation.

Constraints:
- A chat room has only one public conversation.
- A chat room *always* has a public conversation.
- A user can send and receive messages on a private conversation only if she is one of the two participants.

# Building and Running

./mvnw spring-boot:run

  
