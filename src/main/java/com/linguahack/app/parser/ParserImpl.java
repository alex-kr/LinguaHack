package com.linguahack.app.parser;

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


    public TextStats parse(String inputText, long timestamp) {

        return new TextStats(calcTempo(inputText, timestamp),
                calcSaturation(inputText),
                calcLength(inputText),
                calcArtistry(inputText),
                calcActivity(inputText),
                getWordsAmountMap(inputText),
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

  /*  public String getProcessedSentence(String inputText) {
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

        return null;
    }*/

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
            result = charCount / timestamp;
        } else {
            result = 0;
        }

        return result;
    }

    public double calcSaturation(String inputText) {
        return 0.0;
    }

    public int calcLength(String inputText) {
        return inputText.split("[^a-z]+").length;
    }

    public double calcArtistry(String inputText) {
        return 0.0;
    }

    public double calcActivity(String inputText) {
        return 0.0;
    }

    public Map<String, Integer> getWordsAmountMap(String inputText) {
        return new HashMap<String, Integer>();
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


}
