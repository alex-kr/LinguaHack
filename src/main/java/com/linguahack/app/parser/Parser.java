package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;


public interface Parser {
    TextStats parse(String inputText, long timestamp);
}
