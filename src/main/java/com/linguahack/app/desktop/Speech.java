package com.linguahack.app.desktop;

/**
 * Created by Roman Stetsiuk on 10/3/15.
 */
public class Speech {
    private int length;
    private String text;


    public Speech() {
    }

    public Speech(int length, String text) {
        this.length = length;
        this.text = text;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
