package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;

import java.lang.Long;
import java.util.*;

public class ParserImpl implements Parser{


    public TextStats parse(String inputText, long timestamp) {

        return new TextStats(calcTempo(inputText, timestamp),
                             calcSaturation(inputText),
                             calcLength(inputText),
                             calcArtistry(inputText),
                             calcActivity(inputText),
                             getWordsAmountMap(inputText),
                             getLettersAmountMap(inputText)
                            );
    }

    public double calcTempo(String inputText, long timestamp) {
        return 0.0;
    }

    public double calcSaturation(String inputText) {
        return 0.0;
    }

    public int calcLength(String inputText) {
        return 0;
    }

    public double calcArtistry(String inputText) {
        return 0.0;
    }

    public double calcActivity(String inputText) {
        return 0.0;
    }

    public Map<String, Integer> getWordsAmountMap(String inputText) {
        return new HashMap<>();
    }

    public Map<Character, Integer> getLettersAmountMap(String inputText) {
        return new HashMap<>();
    }


}
