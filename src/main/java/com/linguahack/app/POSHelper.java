package com.linguahack.app;

public class POSHelper {
    public static String getPartOfSpeech(String pos) {
        switch (pos) {
            case "CD":
            case "NN":
            case "NNS":
            case "NNP":
            case "NNPS":
            case "PRP":
                return "_noun";
            case "MD":
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                return "_verb";
            case "JJ":
            case "JJR":
            case "JJS":
            case "POS":
            case "PRP$":
                return "_adjective";
            case "EX":
            case "RB":
            case "RBR":
            case "RBS":
                return "_adverb";
            default:
                return "";
        }
    }
}
