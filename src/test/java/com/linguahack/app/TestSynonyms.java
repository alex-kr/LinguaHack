package java.com.linguahack.app;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestSynonyms {
    public static void main(String[] args) throws IOException {

        Map<String, Integer> w1 = new HashMap<String, Integer>();
        w1.put("girl_noun", 4);
        w1.put("boy_noun", 4);
        w1.put("red_adjective", 4);
        w1.put("cruel_adjective", 4);
        w1.put("act_verb", 4);


        Map<String, Integer> w2 = new HashMap<String, Integer>();
        w2.put("girl_noun", 4);
        w2.put("fille_noun", 4);
        w2.put("red_adjective", 4);
        w2.put("brutal_adjective", 4);
        w2.put("do_verb", 4);

        System.out.println(passesSynonymy(w1, w2));

    }

    private static double passesSynonymy(Map<String, Integer> w1, Map<String, Integer> w2) {
        try {
            Set<String> words1 = preprocessWords(w1);
            Set<String> words2 = new HashSet<>();
            for (Map.Entry<String, Integer> entry : w2.entrySet()) {
                if (entry.getValue() > 3) {
                    words2.add(entry.getKey());
                }
            }
            int hits = 0;
            for (String s : words1) {
                if (words2.contains(s)) {
                    hits++;
                }
            }
            System.out.println(hits + "   " + words2.size());
            return ((double) hits / (double) words2.size() * 100.0) > 30.0 ? 10.0 : 0.0;
        } catch (IOException ignored) {
            return 0.0;
        }
    }

    private static Set<String> preprocessWords(Map<String, Integer> w1) throws IOException {
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Integer> entry : w1.entrySet()) {
            if (entry.getValue() >= 3) {
                result.add(entry.getKey());
            }
        }
        // spreading with synonyms
        URL wordnetPath = new URL("file", null, "/usr/local/WordNet-3.0/dict");
        IDictionary dict = new Dictionary(wordnetPath);
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
        throw new RuntimeException();
    }
}
