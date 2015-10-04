package com.linguahack.app.desktop;

/**
 * Created by Roman Stetsiuk on 8/6/15.
 */
public class RequestData {
    private Speech speech1;
    private Speech speech2;

    public RequestData() {
    }

    public RequestData(Speech speech1, Speech speech2) {
        this.speech1 = speech1;
        this.speech2 = speech2;
    }

    public Speech getSpeech1() {
        return speech1;
    }

    public void setSpeech1(Speech speech1) {
        this.speech1 = speech1;
    }

    public Speech getSpeech2() {
        return speech2;
    }

    public void setSpeech2(Speech speech2) {
        this.speech2 = speech2;
    }
}
