package com.linguahack.app.algorithm;

import com.linguahack.app.core.TextStats;

import java.util.HashMap;
import java.util.Map;

public class AlgorithmMockImpl implements Algorithm {

    public Map<String, Double> process(TextStats t1, TextStats t2) {
        Map<String, Double> test = new HashMap<String, Double>();
        test.put("home", 99.7);
        test.put("work", 54.2);
        test.put("chill", 27.1);
        test.put("sex", 29.8);
        test.put("friends", 93.0);
        return test;
    }
}
