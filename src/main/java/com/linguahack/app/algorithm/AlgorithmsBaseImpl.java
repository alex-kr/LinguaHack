package com.linguahack.app.algorithm;

import com.linguahack.app.core.Dialog;
import com.linguahack.app.core.TextStats;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import static com.linguahack.app.algorithm.AlgorithmsBaseImpl.Parameters.*;
import static com.linguahack.app.algorithm.Letter.IDEAL_CAT_1;
import static com.linguahack.app.algorithm.Letter.IDEAL_CAT_2;

public class AlgorithmsBaseImpl implements Algorithm {
    private final Config config = Config.getDefault();

    enum Parameters {
        TEMPO, SATURATION, CONSISTENCY, LENGTH, ARTISTRY, ACTIVITY, SYNONYMY, ANTONYMY, SINTONITY, ASINTONITY, SEMANTICS
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
        private int minRequiredWordsQuantity = 3;

        public int getMinRequiredWordsQuantity() {
            return minRequiredWordsQuantity;
        }

        double criteria(Parameters parameter) {
            return param2info.get(parameter).criteria;
        }

        double weight(Parameters parameter) {
            return param2info.get(parameter).weight;
        }

        public static Config getDefault() {
            Config defaultConfig = new Config();
            defaultConfig.param2info.put(TEMPO, new ParameterInfo(80.0, 10.0));
            defaultConfig.param2info.put(SATURATION, new ParameterInfo(80.0, 5.0));
            defaultConfig.param2info.put(CONSISTENCY, new ParameterInfo(90.0, 10.0));
            defaultConfig.param2info.put(LENGTH, new ParameterInfo(70.0, 5.0));
            defaultConfig.param2info.put(ARTISTRY, new ParameterInfo(90.0, 10.0));
            defaultConfig.param2info.put(ACTIVITY, new ParameterInfo(90.0, 10.0));
            defaultConfig.param2info.put(SYNONYMY, new ParameterInfo(30.0, 10.0));
            defaultConfig.param2info.put(ANTONYMY, new ParameterInfo(80.0, 10.0));
            defaultConfig.param2info.put(SEMANTICS, new ParameterInfo(0.0, 10.0));
            defaultConfig.param2info.put(SINTONITY, new ParameterInfo(0.0, 10.0));
            defaultConfig.param2info.put(ASINTONITY, new ParameterInfo(0.0, 10.0));
            return defaultConfig;
        }
    }

    @Override
    public Map<String, Double> process(TextStats t1, TextStats t2) {
        Map<String, Double> result = new HashMap<>();
        Dialog dialog = computeDialog(t1, t2);
        result.put("base", base(dialog));
        result.put("development", sphereDevelopment(dialog));
        result.put("trusty", sphereTrust(dialog));
        result.put("emotions", sphereEmotions(dialog));
        result.put("leadership", sphereLeaderShip(dialog));
        return result;
    }

    private Dialog computeDialog(TextStats t1, TextStats t2) {
        double tempo = passesTempo(t1.getTempo(), t2.getTempo());
        double saturation = passesSaturation(t1.getSaturation(), t2.getSaturation());
        double length = passesLength(t1.getLength(), t2.getLength());
        double consistency = passesConsistency(t1.getConsistency(), t2.getConsistency());
        double artistry = passesArtistry(t1.getArtistry(), t2.getArtistry());
        double activity = passesActivity(t1.getActivity(), t2.getActivity());
        double synonymy = passesSynonymy(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        double antonymy = passesAntonymy(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        double semantics = passesSemantics(t1.getWordsAmountMap(), t2.getWordsAmountMap());
        double sinton = passesSinton(t1.getLettersAmountMap(), t2.getLettersAmountMap());

        return new Dialog(tempo, saturation, consistency, length,
                artistry, activity, synonymy, antonymy, semantics, sinton);
    }

    private double base(Dialog dialog) {
        double result = 0.0;
        result += dialog.tempo;
        result += dialog.saturation;
        result += dialog.consistency;
        result += dialog.length;
        result += dialog.artistry;
        result += dialog.activity;
        result += dialog.synonymy;
        result += dialog.antonymy;
        result += dialog.semantics;
        result += dialog.sinton;
        return result;
    }

    private double sphereDevelopment(Dialog dialog) {
        double result = 0.0;
        result += dialog.saturation;
        result += dialog.consistency;
        result += dialog.activity;
        result += dialog.synonymy;
        result += dialog.antonymy;
        result += dialog.semantics;
        return result;
    }

    private double sphereTrust(Dialog dialog) {
        double result = 0.0;
        result += dialog.consistency;
        result += dialog.artistry;
        result += dialog.synonymy;
        result += dialog.antonymy;
        result += dialog.sinton;
        return result;
    }

    private double sphereEmotions(Dialog dialog) {
        double result = 0.0;
        result += dialog.tempo;
        result += dialog.artistry;
        result += dialog.semantics;
        result += dialog.sinton;
        return result;
    }

    private double sphereLeaderShip(Dialog dialog) {
        double result = 0.0;
        result += dialog.tempo;
        result += dialog.length;
        result += dialog.activity;
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private double passesTempo(double t1, double t2) {
        return ifPassesCriteria(t1, t2, config.criteria(TEMPO)) ? config.weight(TEMPO) : 0.0;
    }

    private double passesSaturation(double s1, double s2) {
        return ifPassesCriteria(s1, s2, config.criteria(SATURATION)) ? config.weight(SATURATION) : 0.0;
    }

    private double passesConsistency(double c1, double c2) {
        return ifPassesCriteria(c1, c2, config.criteria(CONSISTENCY)) ? config.weight(CONSISTENCY) : 0.0;
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
        if (w1.isEmpty() || w2.isEmpty()) {
            return 0.0;
        }
        try {
            Set<String> words1 = preprocessSynonyms(w1);
            Set<String> words2 = new HashSet<>();
            for (Entry<String, Integer> entry : w2.entrySet()) {
                if (entry.getValue() > config.getMinRequiredWordsQuantity()) {
                    words2.add(entry.getKey());
                }
            }
            int hits = 0;
            for (String s : words1) {
                if (words2.contains(s)) {
                    hits++;
                }
            }
            return (double) hits / (double) words2.size() * 100.0 > config.criteria(SYNONYMY) ? config.weight(SYNONYMY) : 0.0;
        } catch (IOException ignored) {
            return 0.0;
        }
    }

    private double passesAntonymy(Map<String, Integer> w1, Map<String, Integer> w2) {
        if (w1.isEmpty() || w2.isEmpty()) {
            return config.weight(ANTONYMY);
        }
        try {
            Set<String> antonyms = preProcessAntonyms(w1);
            int hits = 0;
            for (Entry<String, Integer> entry : w2.entrySet()) {
                if (entry.getValue() > config.getMinRequiredWordsQuantity()
                        && antonyms.contains(entry.getKey())) {
                    hits++;
                }
            }
            return 100.0 - (double) hits / (double) w2.size() * 100.0 > config.criteria(ANTONYMY)
                    ? config.weight(ANTONYMY) : 0.0;
        } catch (IOException ignored) {
            return 0.0;
        }
    }

    private double passesSemantics(Map<String, Integer> w1, Map<String, Integer> w2) {
        if (w1.isEmpty() || w2.isEmpty()) {
            return 0.0;
        }

        int total1 = w1.size();

        // с учетом позитивного и негативного использования слов?? eto kak voobshe?

        int straightHits = 0;
        int synonymHits = 0;
        int antonymsHits = 0;

        try {
            Set<String> synonyms2 = preprocessSynonyms(w2);
            Set<String> antonyms2 = preProcessAntonyms(w2);


            for (Entry<String, Integer> entry : w1.entrySet()) {
                if (w2.containsKey(entry.getKey())) {
                    straightHits++;
                } else if (synonyms2.contains(entry.getKey())) {
                    synonymHits++;
                } else if (antonyms2.contains(entry.getKey())) {
                    antonymsHits++;
                }
            }

            return (2.0 * straightHits + synonymHits - 2.0 * antonymsHits) / total1;
        } catch (IOException ignored) {
            return 0.0;
        }
    }

    private double passesSinton(Map<Character, Integer> l1, Map<Character, Integer> l2) {
        int total1 = 0;
        for (Entry<Character, Integer> entry : l1.entrySet()) {
            total1 += entry.getValue();
        }

        int total2 = 0;
        for (Entry<Character, Integer> entry : l1.entrySet()) {
            total2 += entry.getValue();
        }

        Set<Character> preferable1 = new HashSet<>();
        Set<Character> ignored1 = new HashSet<>();
        splitLetters(l1, total1, preferable1, ignored1);


        Set<Character> preferable2 = new HashSet<>();
        Set<Character> ignored2 = new HashSet<>();
        splitLetters(l2, total2, preferable2, ignored2);

        int points = 0;

        for (Character c : preferable1) {
            // TODO algortihm??
            if (preferable2.contains(c)) {
                points++;
            } else if (ignored2.contains(c)) {
                points--;
            }
        }

        double result = 0.0;
        result += points > config.criteria(SINTONITY) ? config.weight(SINTONITY) : 0.0;

        points = 0;
        for (Character c : ignored1) {
            // TODO algortihm??
            if (ignored2.contains(c)) {
                points++;
            } else if (preferable2.contains(c)) {
                points--;
            }
        }

        result += points > config.criteria(ASINTONITY) ? config.weight(ASINTONITY) : 0.0;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Set<String> preprocessSynonyms(Map<String, Integer> w1) throws IOException {
        Set<String> result = new HashSet<>();
        for (Entry<String, Integer> entry : w1.entrySet()) {
            if (entry.getValue() >= config.getMinRequiredWordsQuantity()) {
                result.add(entry.getKey());
            }
        }
        // spreading with synonyms
        URL wordNetPath = new URL("file", null, "src/resources/dict");
        IDictionary dict = new Dictionary(wordNetPath);
        dict.open();

        Set<String> synonyms = new HashSet<>();
        for (String sample : result) {

            int underscore = sample.indexOf('_');
            String word = sample.substring(0, underscore);
            String part = sample.substring(underscore + 1);


            IIndexWord idxWord = dict.getIndexWord(word, definePos(part));
            IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning

            IWord iWord = dict.getWord(wordID);
            ISynset synset = iWord.getSynset();

            for (IWord w : synset.getWords()) {
                synonyms.add(w.getLemma() + "_" + part);
            }
        }

        result.addAll(synonyms);

        return result;
    }

    private Set<String> preProcessAntonyms(Map<String, Integer> w1) throws IOException {
        Set<String> result = new HashSet<>();
        for (Entry<String, Integer> entry : w1.entrySet()) {
            if (entry.getValue() >= config.getMinRequiredWordsQuantity()) {
                result.add(entry.getKey());
            }
        }

        URL wordNetPath = new URL("file", null, "src/resources/dict");
        IDictionary dict = new Dictionary(wordNetPath);
        dict.open();

        Set<String> antonyms = new HashSet<>();
        for (String sample : result) {

            int underscore = sample.indexOf('_');
            String word = sample.substring(0, underscore);
            String part = sample.substring(underscore + 1);


            IIndexWord idxWord = dict.getIndexWord(word, definePos(part));

            IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning

            IWord iWord = dict.getWord(wordID);

            Map<IPointer, List<IWordID>> relatedMap = iWord.getRelatedMap();

            for (Entry<IPointer, List<IWordID>> entry : relatedMap.entrySet()) {
                for (IWordID wordID1 : entry.getValue()) {
                    IWord iWord1 = dict.getWord(wordID1);
                    antonyms.add(iWord1.getLemma() + "_" + part);
                }
            }
        }

        return antonyms;
    }

    private POS definePos(String pos) {
        if (pos.equals("noun")) {
            return POS.NOUN;
        }
        if (pos.equals("verb")) {
            return POS.VERB;
        }
        if (pos.equals("adjective")) {
            return POS.ADJECTIVE;
        }
        if (pos.equals("adverb")) {
            return POS.ADVERB;
        }
        throw new RuntimeException();
    }

    private void splitLetters(Map<Character, Integer> l1, int total1, Set<Character> preferable1, Set<Character> ignored1) {
        for (Entry<Character, Integer> entry : l1.entrySet()) {
            double percent = (double) entry.getValue() / total1;

            double idealValue = IDEAL_CAT_1.containsKey(entry.getKey())
                    ? IDEAL_CAT_1.get(entry.getKey()) : IDEAL_CAT_2.get(entry.getKey());

            double relative = percent / idealValue;

            if (relative > 1.05) {
                preferable1.add(entry.getKey());
            } else if (relative < 0.95) {
                ignored1.add(entry.getKey());
            } else {
                // TODO wat? if in mid
            }
        }
    }

    private boolean ifPassesCriteria(double v1, double v2, double criteria) {
        double percent = Math.max(v1, v2) / 100.0;
        double value = Math.min(v1, v2) / percent;
        return value > criteria;
    }

}