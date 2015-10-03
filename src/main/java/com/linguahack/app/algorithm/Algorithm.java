package com.linguahack.app.algorithm;


import com.linguahack.app.core.Text;

import java.util.Map;

public interface Algorithm {
    Map<String, Double> process(Text t1, Text t2);
}
