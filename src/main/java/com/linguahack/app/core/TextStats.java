package com.linguahack.app.core;

import java.util.List;
import java.util.Set;

public class TextStats {
    private final double tempo;
    private final int saturation;
    private final int length;
    private final double artistry;
    private final double activity;

    private final Set<String> words;
    private final List<Character> letters;

    public TextStats(double tempo, int saturation, int length, double artistry, double activity,
                     Set<String> words, List<Character> letters) {
        this.tempo = tempo;
        this.saturation = saturation;
        this.length = length;
        this.artistry = artistry;
        this.activity = activity;

        this.words = words;
        this.letters = letters;
    }

    public double getTempo() {
        return tempo;
    }

    public int getSaturation() {
        return saturation;
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

    public Set<String> getWords() {
        return words;
    }

    public List<Character> getLetters() {
        return letters;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}

