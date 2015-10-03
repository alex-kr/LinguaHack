package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;

import java.util.ArrayList;
import java.util.HashSet;

public class ParserMockImpl implements Parser {
    public TextStats parse(String inputText) {
        return new TextStats(0.0, 1, 2, 2, 2, new HashSet<>(), new ArrayList<>());
    }
}
