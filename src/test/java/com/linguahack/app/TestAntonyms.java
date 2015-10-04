package java.com.linguahack.app;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TestAntonyms {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> w1 = new HashMap<String, Integer>();
        w1.put("car_noun", 4);
        w1.put("boy_noun", 4);
        w1.put("good_adjective", 4);
        w1.put("polite_adjective", 4);
        w1.put("write_verb", 4);

        Set<String> ant1 = preProcessAntonyms(w1);
        for (String s : ant1) {
            System.out.println(s);
        }


        Map<String, Integer> w2 = new HashMap<String, Integer>();
        w2.put("girl_noun", 4);
        w2.put("baloon_noun", 4);
        w2.put("red_adjective", 4);
        w2.put("impolite_adjective", 4);
        w2.put("do_verb", 4);

        System.out.println(passesAntonymy(w1, w2));
    }

    private static double passesAntonymy(Map<String, Integer> w1, Map<String, Integer> w2) {
        try {
            Set<String> antonyms = preProcessAntonyms(w1);
            int hits = 0;
            for (Map.Entry<String, Integer> entry : w2.entrySet()) {
                if (entry.getValue() > 3
                        && antonyms.contains(entry.getKey())) {
                    hits++;
                }
            }

            System.out.println(hits + "  " + w2.size());
            return 100.0 - (double) hits / (double) w2.size() * 100.0 < 80.0
                    ? 00.0 : 10.0;
        } catch (IOException ignored) {
            return 0.0;
        }
    }

    private static Set<String> preProcessAntonyms(Map<String, Integer> w1) throws IOException {
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Integer> entry : w1.entrySet()) {
            if (entry.getValue() >= 3) {
                result.add(entry.getKey());
            }
        }

        URL wordNetPath = new URL("file", null, "/usr/local/WordNet-3.0/dict");
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

            for (Map.Entry<IPointer, List<IWordID>> entry : relatedMap.entrySet()) {
                for (IWordID wordID1 : entry.getValue()) {
                    IWord iWord1 = dict.getWord(wordID1);
                    antonyms.add(iWord1.getLemma() + "_" + part);
                }
            }
        }

        return antonyms;
    }

    private static POS definePos(String pos) {
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
}
