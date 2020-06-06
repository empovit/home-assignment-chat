package com.github.empovit.roomchat.conversations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ConversationMeta {

    private final String id;

    @EqualsAndHashCode.Exclude // description may change, e.g. set by a user
    private String description;
}
