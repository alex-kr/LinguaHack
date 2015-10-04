package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;

import java.util.HashMap;

public class ParserMockImpl implements Parser {
    public TextStats parse(String inputText, long timestamp) {
        return new TextStats(0.0, 1, 2, 4, 2, 2, new HashMap<String, Integer>(), new HashMap<Character, Integer>());
    }
}
