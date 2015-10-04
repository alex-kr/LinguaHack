package com.linguahack.app.to;

public class SpeechTO {

    Long length;
    String text;

    public SpeechTO(Long length, String text) {
        this.length = length;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}

