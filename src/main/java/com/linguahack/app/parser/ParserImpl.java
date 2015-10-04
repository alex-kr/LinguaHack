package com.linguahack.app.parser;

import com.linguahack.app.POSHelper;
import com.linguahack.app.core.TextStats;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserImpl implements Parser {

    private class Holder {
        String[] tokens;
        String[] tags;
    }


    public TextStats parse(String inputText, long timestamp) {

        Holder holder = new Holder();
        holder.tokens = getTokens(inputText);
        holder.tags   = getTags(holder.tokens);

        return new TextStats(calcTempo(inputText, timestamp),
                             calcSaturation(inputText),
                             calcConsistency(inputText),
                             calcLength(inputText),
                             calcArtistry(holder),
                             calcActivity(holder),
                             getWordsAmountMap(holder),
                             getLettersAmountMap(inputText)
        );
    }



    public List<String> getSentences(String inputText) {
        InputStream modelIn = null;
        SentenceModel model = null;

        try {
            modelIn = new FileInputStream("src/resources/da-sent.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            model = new SentenceModel(modelIn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

        String sentences[] = sentenceDetector.sentDetect(inputText);

        return Arrays.asList(sentences);
    }

    public String[] getTokens(String inputText) {

        InputStream modelIn = null;
        TokenizerModel model = null;
        Tokenizer tokenizer;

        try {
            modelIn = new FileInputStream("src/resources/en-token.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            model = new TokenizerModel(modelIn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        tokenizer = new TokenizerME(model);

        return tokenizer.tokenize(inputText);
    }


    public String[] getTags(String[] tokens) {
        InputStream modelIn = null;
        POSModel model = null;

        try {
            modelIn = new FileInputStream("src/resources/en-pos-maxent.bin");
            model = new POSModel(modelIn);
        }
        catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        POSTaggerME tagger = new POSTaggerME(model);

        return tagger.tag(tokens);
    }



    public double calcTempo(String inputText, long timestamp) {
        int charCount = 0;
        int charCode;
        double result;


        for (int i = 0; i < inputText.length(); i++) {
            charCode = (int) inputText.charAt(i);

            if ((charCode >= 97 && charCode <= 122))
                charCount++;
        }

        if (timestamp > 0) {
            result = (double)charCount / timestamp;
        } else {
            result = 0.0;
        }

        return result;
    }

    public double calcSaturation(String inputText) {
        int wordsAmount = calcLength(inputText);
        int sentencesAmount = inputText.split("[.]").length;

        return wordsAmount/sentencesAmount;

    }

    public double calcConsistency(String inputText) {
        List<String> sentences = getSentences(inputText);
        int sentAmount = sentences.size();
        int counOfminingfulWords = 0;
        double result;
        String[] tags = null;

        for (String sentence: sentences) {
            tags = getTags(getTokens(sentence));
            for (int i=0; i < tags.length ; i++ ) {
                if (POSHelper.getPartOfSpeech(tags[i]) != "") {
                    counOfminingfulWords++;
                }
            }
        }

        if (sentAmount != 0) {
            result = (double)counOfminingfulWords/sentAmount;
        } else {
            return 0.0;
        }
        return result;
    }

    public int calcLength(String inputText) {
        return inputText.split("[^a-z]+").length;
    }

    public double calcArtistry(Holder holder) {
        String[] tokens = holder.tokens;
        String[] tags = holder.tags;
        int overalWordsNumber = tokens.length;
        int wordsCounter = 0;
        double result;

        for (int i =0; i < overalWordsNumber; i++) {
            if ((POSHelper.getPartOfSpeech(tags[i]) == "_adjective") ||
                (POSHelper.getPartOfSpeech(tags[i]) == "_adverb"   )) {
                wordsCounter++;
            }
        }

        if (overalWordsNumber != 0) {
            result = (double)wordsCounter/overalWordsNumber;
        } else {
            result = 0.0;
        }
        return result;
    }

    public double calcActivity(Holder holder) {
        String[] tokens = holder.tokens;
        String[] tags = holder.tags;
        int overalWordsNumber = tokens.length;
        int wordsCounter = 0;
        double result;

        for (int i = 0; i < overalWordsNumber; i++) {
            if (POSHelper.getPartOfSpeech(tags[i]) == "_verb") {
                wordsCounter++;
            }
        }

        if (overalWordsNumber != 0) {
            result = (double)wordsCounter/overalWordsNumber;
        } else {
            result = 0.0;
        }
        return result;
    }

    public Map<String, Integer> getWordsAmountMap(Holder holder) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        String[] tokens = holder.tokens;
        String[] tags = holder.tags;
        int length = tokens.length;
        String tmpWord;

        for (int i = 0; i < length; i++) {
            if (tags[i] != "") {
                tmpWord = tokens[i] + POSHelper.getPartOfSpeech(tags[i]);
                if (result.containsKey(tmpWord)) {
                    result.put(tmpWord, result.get(tmpWord) + 1);
                } else {
                    result.put(tmpWord, 1);
                }
            }
        }

        return result;
    }

    public Map<Character, Integer> getLettersAmountMap(String inputText) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();

        char letter;

        for (int i = 0; i < inputText.length(); i++) {
            letter = inputText.charAt(i);
            if (((int) letter >= 97 && (int) letter <= 122)) {
                if (map.containsKey(letter)) {
                    map.put(letter, map.get(letter) + 1);
                } else {
                    map.put(letter, 1);
                }
            }
        }

        return map;
    }

    /*public double calcConsistency(String inputText) {
        String[] tokens = getTokens(inputText);
        int overalWordsNumber = tokens.length;
        int wordsCounter = 0;
        double result;

        for (int i =0; i < overalWordsNumber; i++) {
            if (POSHelper.getPartOfSpeech(tokens[i]) != ""){
                wordsCounter++;
            }
        }

        if (overalWordsNumber != 0) {
            result = wordsCounter/overalWordsNumber;
        } else {
            result = 0.0;
        }
        return result;
    }*/


}
