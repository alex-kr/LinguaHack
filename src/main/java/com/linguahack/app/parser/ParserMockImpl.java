package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;

public class ParserMockImpl implements Parser {
    public TextStats parse(String inputText) {
        return new TextStats();
    }
}
