package com.linguahack.app.parser;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import org.hamcrest.Matcher;
import org.junit.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParserImplTest {

    ParserImpl parser;
    String inputText;
    String inputText2;

    @Before
    public void init(){
        parser = new ParserImpl();
        inputText = "She cannot find their beach towels. ";
        inputText = inputText.toLowerCase();
        inputText2 = "She cannot find their beach towels? " +
                     "Donna always loses things. " +
                     "The towels are not in the laundry " +
                     "basket or dryer. They are not " +
                     "in the closet, either. ";
        inputText2 = inputText2.toLowerCase();
    }

    @Test
    public void test_calcTempo() {
        Assert.assertThat(0.0, is(parser.calcTempo(inputText, 0)));
        Assert.assertThat(29.0, is(parser.calcTempo(inputText, 2)));
    }

    @Test
    public void test_calcLength() {
        Assert.assertThat(6, is(parser.calcLength(inputText)));
    }

    @Test
    public void test_getLetterAmountMap() {
        Map<Character, Integer> map = parser.getLettersAmountMap(inputText);
        Assert.assertThat(2, is(map.get('s')));
        Assert.assertThat(3, is(map.get('h')));
        Assert.assertThat(4, is(map.get('e')));
        Assert.assertThat(2, is(map.get('c')));
        Assert.assertThat(2, is(map.get('a')));
        Assert.assertThat(3, is(map.get('n')));
    }

    @Test
    public void  test_getSentences() {
        List<String> list = parser.getSentences(inputText2);

        Assert.assertThat("she cannot find their beach towels?" , is(list.get(0)));
        Assert.assertThat("donna always loses things." , is(list.get(1)));
    }

    @Test
    public void  test_getTokens() {
        String input = "Azaza aaa bbb ooo";

        String[] tokens= parser.getTokens(input);

        Assert.assertThat("Azaza" , is(tokens[0]));
        Assert.assertThat("aaa" , is(tokens[1]));
        Assert.assertThat("bbb" , is(tokens[2]));
        Assert.assertThat("ooo" , is(tokens[3]));
    }

    @Test
    public void  test_getTags() {

        String[] tokens= new String[]{"Most", "large", "cities", "in", "the", "US", "had",
                                      "morning", "and", "afternoon", "newspapers", "."};	;

        String[] tags = parser.getTags(tokens);

        Assert.assertThat("JJS" , is(tags[0]));
        Assert.assertThat("JJ" , is(tags[1]));
        Assert.assertThat("NNS" , is(tags[2]));
        Assert.assertThat("IN" , is(tags[3]));
        Assert.assertThat("DT" , is(tags[4]));
        Assert.assertThat("NNP" , is(tags[5]));
        Assert.assertThat("VBD" , is(tags[6]));


    }

    @Test
    public void  test_calcSaturation() {
        String input = "she cannot find their beach at towels. " +
                "donna always loses things.";

        Assert.assertThat(5.0 , is(parser.calcSaturation(input)));
    }

}
