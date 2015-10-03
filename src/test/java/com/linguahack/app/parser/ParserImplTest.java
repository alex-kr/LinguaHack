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

    @Before
    public void init(){
        parser = new ParserImpl();
        inputText = "She cannot find their beach towels. ";
        inputText = inputText.toLowerCase();
    }

    @Test
    public void test_calcTempo() {
        Assert.assertThat(0.0, is(parser.calcTempo(inputText, 0)));
        Assert.assertThat(29.0, is(parser.calcTempo(inputText, 1)));
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


}
