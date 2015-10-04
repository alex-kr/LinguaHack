package com.linguahack.app.to;

public class PackageTO {

    SpeechTO speech1;
    SpeechTO speech2;

    public PackageTO(SpeechTO speech1, SpeechTO speech2) {
        this.speech1 = speech1;
        this.speech2 = speech2;
    }

    public SpeechTO getSpeech1() {
        return speech1;
    }

    public void setSpeech1(SpeechTO speech1) {
        this.speech1 = speech1;
    }

    public SpeechTO getSpeech2() {
        return speech2;
    }

    public void setSpeech2(SpeechTO speech2) {
        this.speech2 = speech2;
    }
}
