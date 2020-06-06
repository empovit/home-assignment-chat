# Introduction

Simple room chat application based on WebSocket, with public room conversations and private direct messages.

It is what I could come up with after working on a home assignment for ~15 hours. 

# Building and Running

1. Start the server `./mvnw spring-boot:run` (or in remote debug mode 
   `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`)
2. Connect with a WebSocket client, for instance 
   [this](https://chrome.google.com/webstore/detail/websocket-test-client/fgponpodhbmadfljofbimhhlengambbn) 
   Chrome extension.
3. Start issuing commands and receiving messages.

# Supported commands

This chat uses simple text commands that are easy to type.

- /user _name_ - create a new user
- /room _name_ /as _user_ - create a chat room, the user automatically joins the room
- /join _name_ /as _user_ - join an existing chat room
- /chats _user_ - list user's conversations in a room
- /messages _chat_ /as _user_ - show all messages in a chat
- /pub _text_ /in _room_ /as _user_ - send a public message to a chat room
- /send _text_ /in _room_ /to _peer_ /as _user_ - send a direct message to another participant 

# Constraints

- A chat room has only one public conversation.
- A chat room *always* has a public conversation.
- A user can send and receive messages on a private conversation only if she is one of the two participants.

# Sample execution

*NOTE:* Keep in mind that here both _alice_ and _bob_ are using the same connection, therefore we can see all messages
        they exchange between them.

```
/join My room
--
ERROR:	User name must be specified via the 'as' key
/join My room /as alice
--
ERROR:	Unknown user: alice
/user alice
/join
--
ERROR:	Command is malformed: /join
/join My room /as alice
--
ERROR:	Room not found: My room
/room My room /as alice
/room My room /as alice
--
ERROR:	Room already exists: My room
/join My room /as bob
--
ERROR:	Unknown user: bob
/user bob
/join My room /as bob
/send Hi, Bob! /as alice
--
ERROR:	Room name must be specified
/send Hi, Bob! /as alice /in My room
--
ERROR:	Recipient must be specified
/send Hi, Bob! /as alice /in My room /to bob
--
ID:	2d181a58-0d05-428e-90a1-4670c37ae406
TIME:	1591473995583
ROOM:	My room
FROM:	alice
TO:	bob
TEXT:	Hi, Bob!
/pub Hi, everyone! /as alice /in My room
--
ID:	17b56a9e-46f8-481a-a0c8-4d165d59b53c
TIME:	1591474018477
ROOM:	My room
FROM:	alice
TEXT:	Hi, everyone!
/chats alice
--
ID:	2be2cfb5-fbd3-4255-bccb-5afa371b3295
ABOUT:	Private with user "bob"
--
ID:	b3223507-88b6-467c-b49d-58e2f52021af
ABOUT:	Public in room "My room"
/messages 82d8f4b1-e8d0-45c5-842a-3eedd8f61483 /as alice
--
ERROR:	Conversation not found: 82d8f4b1-e8d0-45c5-842a-3eedd8f61483
/send Bye for now :( /as bob /to alice /in My room
--
ID:	13280269-a637-4854-9264-439cb492e1b7
TIME:	1591474166109
ROOM:	My room
FROM:	bob
TO:	alice
TEXT:	Bye for now :(
/messages 2be2cfb5-fbd3-4255-bccb-5afa371b3295 /as alice
--
ID:	2d181a58-0d05-428e-90a1-4670c37ae406
TIME:	1591473995583
ROOM:	My room
FROM:	alice
TO:	bob
TEXT:	Hi, Bob!
--
ID:	13280269-a637-4854-9264-439cb492e1b7
TIME:	1591474166109
ROOM:	My room
FROM:	bob
TO:	alice
TEXT:	Bye for now :(
/room Another /as bob
/pub Can anybody see it? /in Another /as bob
/chats alice
--
ID:	2be2cfb5-fbd3-4255-bccb-5afa371b3295
ABOUT:	Private with user "bob"
--
ID:	b3223507-88b6-467c-b49d-58e2f52021af
ABOUT:	Public in room "My room"
/chats bob
--
ID:	aaf23a3b-d61d-4d24-ac75-99567325a445
ABOUT:	Private with user "alice"
--
ID:	03606b60-b1b7-434d-a84d-ea745a9af890
ABOUT:	Public in room "Another"
--
ID:	b3223507-88b6-467c-b49d-58e2f52021af
ABOUT:	Public in room "My room"
```

# TODO

- Tests, tests, tests
- Authentication
- Make it reactive (e.g. using WebFlux)

This functionality could be nice to have:

- A user can leave a chat room.
- A user can see who has joined the room.
