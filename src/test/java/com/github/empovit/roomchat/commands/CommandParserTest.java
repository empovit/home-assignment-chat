package com.github.empovit.roomchat.commands;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    public void whenCommandHasOneElementItIsParsed() {
        Map<String, String> commands = new CommandParser().parse("/hello world").getAttributes();
        assertEquals(1, commands.size());
    }

    @Test
    public void whenCommandHasTwoElementsTheyAreParsed() {
        Map<String, String> commands = new CommandParser().parse("/hello world /and bye").getAttributes();
        assertEquals(2, commands.size());
    }

    @Test
    public void whenCommandIsEmptyThrowError() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParser().parse("").getAttributes());
    }

    @Test
    public void whenCommandDoesNotStartWithSlashThrowError() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParser().parse("hello world"));
    }

    @Test
    public void whenCommandContainsOneWordThrowError() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParser().parse("/hello"));
    }
}