package com.linguahack.app.core;

import java.util.Map;

public class TextStats {
    private final double tempo;
    private final double saturation;
    private final double consistency;
    private final int length;
    private final double artistry;
    private final double activity;

    private final Map<String, Integer> wordsAmountMap;
    private final Map<Character, Integer> lettersAmountMap;

    public TextStats(double tempo, double saturation, double consistency, int length, double artistry, double activity,
                     Map<String, Integer> wordsAmountMap, Map<Character, Integer> lettersAmountMap) {
        this.tempo = tempo;
        this.saturation = saturation;
        this.consistency = consistency;
        this.length = length;
        this.artistry = artistry;
        this.activity = activity;

        this.wordsAmountMap = wordsAmountMap;
        this.lettersAmountMap = lettersAmountMap;
    }

    public double getTempo() {
        return tempo;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getConsistency() {
        return consistency;
    }

    public int getLength() {
        return length;
    }

    public double getArtistry() {
        return artistry;
    }

    public double getActivity() {
        return activity;
    }

    public Map<String, Integer> getWordsAmountMap() {
        return wordsAmountMap;
    }

    public Map<Character, Integer> getLettersAmountMap() {
        return lettersAmountMap;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}

