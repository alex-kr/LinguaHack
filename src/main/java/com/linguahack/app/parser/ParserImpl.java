package com.linguahack.app.parser;

import com.linguahack.app.core.TextStats;
import org.omg.CORBA.INTERNAL;

import java.util.*;

public class ParserImpl implements Parser{


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

    public double calcTempo(String inputText, long timestamp) {
        int charCount = 0;
        int charCode;
        double result;

        for( int i = 0; i < inputText.length( ); i++ )
        {
            charCode = (int) inputText.charAt( i );

            if( (charCode >= 97 && charCode<= 122))
                charCount++;
        }

        if (timestamp > 0) {
            result = charCount/timestamp;
        } else {
            result = 0;
        }

        return result;
    }

    public double calcSaturation(String inputText) {
        return 0.0;
    }

    public int calcLength(String inputText) {
        return 0;
    }

    public double calcArtistry(String inputText) {
        return 0.0;
    }

    public double calcActivity(String inputText) {
        return 0.0;
    }

    public Map<String, Integer> getWordsAmountMap(String inputText) {
        return new HashMap();
    }

    public Map<Character, Integer> getLettersAmountMap(String inputText) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();

        char letter;

        for( int i=0; i<inputText.length(); i++ ) {
            letter = inputText.charAt(i);
            if(((int) letter >= 97 && (int) letter<= 122)) {
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
