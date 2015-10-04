package com.linguahack.app.algorithm;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

public class Letter {
    public static final Map<Character, Double> IDEAL_CAT_1 = new HashMap<Character, Double>();
    public static final Map<Character, Double> IDEAL_CAT_2 = new HashMap<Character, Double>();

    static {
        IDEAL_CAT_1.put('b', 1.49);
        IDEAL_CAT_1.put('d', 4.25);
        IDEAL_CAT_1.put('g', 2.02);
        IDEAL_CAT_1.put('j', 0.15);
        IDEAL_CAT_1.put('m', 2.41);
        IDEAL_CAT_1.put('n', 6.75);
        IDEAL_CAT_1.put('l', 4.03);
        IDEAL_CAT_1.put('r', 5.99);
        IDEAL_CAT_1.put('v', 0.98);
        IDEAL_CAT_1.put('z', 0.07);

        IDEAL_CAT_2.put('a', 2.23);
        IDEAL_CAT_2.put('h', 6.09);
        IDEAL_CAT_2.put('k', 0.77);
        IDEAL_CAT_2.put('p', 0.77);
        IDEAL_CAT_2.put('q', 0.17);
        IDEAL_CAT_2.put('s', 6.33);
        IDEAL_CAT_2.put('t', 9.06);
        IDEAL_CAT_2.put('x', 0.15);
        IDEAL_CAT_2.put('c', 2.78);
    }
}
