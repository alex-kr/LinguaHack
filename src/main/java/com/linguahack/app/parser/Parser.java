package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;

import java.security.Timestamp;

public interface Parser {
    TextStats parse(String inputText, Timestamp timestamp);
}
