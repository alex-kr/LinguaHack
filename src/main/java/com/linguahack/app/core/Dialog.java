package com.linguahack.app.core;

public class Dialog {
    public final double tempo;
    public final double saturation;
    public final double consistency;
    public final double length;
    public final double artistry;
    public final double activity;
    public final double synonymy;
    public final double antonymy;
    public final double semantics;
    public final double sinton;


    @Override
    public String toString() {
        return "Dialog{" +
                "tempo=" + tempo +
                ", saturation=" + saturation +
                ", consistency=" + consistency +
                ", length=" + length +
                ", artistry=" + artistry +
                ", activity=" + activity +
                ", synonymy=" + synonymy +
                ", antonymy=" + antonymy +
                ", semantics=" + semantics +
                ", sinton=" + sinton +
                '}';
    }

    public Dialog(double tempo, double saturation, double consistency,
                  double length, double artistry, double activity, double synonymy,
                  double antonymy, double semantics, double sinton) {
        this.tempo = tempo;
        this.saturation = saturation;
        this.consistency = consistency;
        this.length = length;
        this.artistry = artistry;
        this.activity = activity;
        this.synonymy = synonymy;
        this.antonymy = antonymy;
        this.semantics = semantics;
        this.sinton = sinton;


    }
}
