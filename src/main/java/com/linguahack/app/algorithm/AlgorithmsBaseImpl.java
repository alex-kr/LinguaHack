package com.linguahack.app.algorithm;

import com.linguahack.app.core.TextStats;

import java.util.HashMap;
import java.util.Map;

import static com.linguahack.app.algorithm.AlgorithmsBaseImpl.Parameters.*;

public class AlgorithmsBaseImpl implements Algorithm {
    private final Config config = Config.getDefault();

    enum Parameters {
        TEMPO, SATURATION, LENGTH, ARTISTRY, ACTIVITY, SYNONYMY, ANTONYMY
    }

    static class ParameterInfo {
        private final double criteria;
        private final double weight;

        public ParameterInfo(double criteria, double weight) {
            this.criteria = criteria;
            this.weight = weight;
        }
    }

    static class Config {
        private final Map<Parameters, ParameterInfo> param2info = new HashMap<>();

        double criteria(Parameters parameter) {
            return param2info.get(parameter).criteria;
        }

        double weight(Parameters parameter) {
            return param2info.get(parameter).weight;
        }

        public static Config getDefault() {
            Config defaultConfig = new Config();
            defaultConfig.param2info.put(TEMPO, new ParameterInfo(80.0, 10.0));
            defaultConfig.param2info.put(SATURATION, new ParameterInfo(80.0, 10.0));
            defaultConfig.param2info.put(LENGTH, new ParameterInfo(70.0, 10.0));
            defaultConfig.param2info.put(ARTISTRY, new ParameterInfo(90.0, 10.0));
            defaultConfig.param2info.put(ACTIVITY, new ParameterInfo(90.0, 10.0));
            defaultConfig.param2info.put(SYNONYMY, new ParameterInfo(30.0, 10.0));
            defaultConfig.param2info.put(ANTONYMY, new ParameterInfo(70.0, 10.0));
            return defaultConfig;
        }
    }

    @Override
    public Map<String, Double> process(TextStats t1, TextStats t2) {
        double base = defineBase(t1, t2);
        return null;
    }

    private double defineBase(TextStats t1, TextStats t2) {
        double result = 0.0;
        result += passesTempo(t1.getTempo(), t2.getTempo());
        result += passesSaturation(t1.getSaturation(), t2.getSaturation());
        result += passesLength(t1.getLength(), t2.getLength());
        result += passesArtistry(t1.getArtistry(), t2.getArtistry());
        result += passesActivity(t1.getActivity(), t2.getActivity());
        result += passesSynonymy(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        result += passesAntonomy(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        result += passesSemantity(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        result += passesSinton(t1.getLettersAmountMap(), t2.getLettersAmountMap());
        result += passesAntiton(t1.getLettersAmountMap(), t2.getLettersAmountMap());
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private double passesTempo(double t1, double t2) {
        return ifPassesCriteria(t1, t2, config.criteria(TEMPO)) ? config.weight(TEMPO) : 0.0;
    }

    private double passesSaturation(double s1, double s2) {
        return ifPassesCriteria(s1, s2, config.criteria(SATURATION)) ? config.weight(SATURATION) : 0.0;
    }

    private double passesLength(double l1, double l2) {
        return ifPassesCriteria(l1, l2, config.criteria(LENGTH)) ? config.weight(LENGTH) : 0.0;
    }

    private double passesArtistry(double a1, double a2) {
        return ifPassesCriteria(a1, a2, config.criteria(ARTISTRY)) ? config.weight(ARTISTRY) : 0.0;
    }

    private double passesActivity(double a1, double a2) {
        return ifPassesCriteria(a1, a2, config.criteria(ACTIVITY)) ? config.weight(ACTIVITY) : 0.0;
    }

    private double passesSynonymy(Map<String, Integer> w1, Map<String, Integer> w2) {
        // TODO
        return 0.0;
    }

    private double passesAntonomy(Map<String, Integer> w1, Map<String, Integer> w2) {
        // TODO
        return 0.0;
    }

    private double passesSemantity(Map<String, Integer> w1, Map<String, Integer> w2) {
        // TODO
        return 0.0;
    }

    private double passesSinton(Map<Character, Integer> l1, Map<Character, Integer> l2) {
        // TODO
        return 0.0;
    }

    private double passesAntiton(Map<Character, Integer> l1, Map<Character, Integer> l2) {
        // TODO
        return 0.0;
    }

    private boolean ifPassesCriteria(double v1, double v2, double criteria) {
        double percent = Math.max(v1, v2) / 100.0;
        double value = Math.min(v1, v2) / percent;
        return value > criteria;
    }

}
