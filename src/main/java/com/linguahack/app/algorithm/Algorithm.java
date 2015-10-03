package com.linguahack.app.algorithm;

import com.linguahack.app.core.TextStats;

import java.util.Map;

public interface Algorithm {
    Map<String, Double> process(TextStats t1, TextStats t2);
}
