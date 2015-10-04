package com.linguahack.app.algorithm;

import com.linguahack.app.algorithm.Algorithm;
import com.linguahack.app.algorithm.AlgorithmsBaseImpl;
import com.linguahack.app.core.TextStats;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestAlgorithm {
    private Algorithm algorithm = new AlgorithmsBaseImpl();

    @Before
    public void setup() {

    }

    @Test
    public void testBase() {
        TextStats base = new TextStats(10, 57.5, 30, 70, 30, 40,
                new HashMap<String, Integer>(), new HashMap<Character, Integer>());
        TextStats passes = new TextStats(9, 60.0, 28, 60, 35, 38,
                new HashMap<String, Integer>(), new HashMap<Character, Integer>());
        TextStats notPasses = new TextStats(1, 300.8, 150, 50, 10, 20,
                new HashMap<String, Integer>(), new HashMap<Character, Integer>());

        Map<String, Double> map = algorithm.process(base, passes);
        assertTrue(map.get("base") > 40.0);

        map = algorithm.process(base, notPasses);
        assertTrue(map.get("base") < 70.0);
    }
}
