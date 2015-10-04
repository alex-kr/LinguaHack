package com.linguahack.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.linguahack.app.algorithm.Algorithm;
import com.linguahack.app.algorithm.AlgorithmsBaseImpl;
import com.linguahack.app.core.TextStats;
import com.linguahack.app.parser.Parser;
import com.linguahack.app.parser.ParserImpl;
import com.linguahack.app.to.PackageTO;

import java.util.Map;

public class Demo {
    static Parser parser = new ParserImpl();
    static Algorithm algorithm = new AlgorithmsBaseImpl();

    public static void main(String[] args) {

        String inputJson = "{\n" +
                "  \"speech1\": {\n" +
                "    \"length\": 100,\n" +
                "    \"text\": \"Nkem, see yourself through my eyes, she how beautiful you are. My dearest, sweetest, adorable darling. What can you see? Is that all you can see? There is more. Look into my heart what do you find? Take a stroll and tell me what you see. What can you see? Thank you, but there is more, what else can you see? Cant you see “NKEM” everywhere in my heart. You did not mention it. Have another look- You take the center stage. See how priceless, esteemed and adorable you are. Look around, there is more\n" +
                "How can you find enough, when there is a lot more to show you? There is more – my NKEM in many beautiful colours Can you see the pool in my heart? Come let me show you around. Here is it. This pool is always cool, calm, soothing and “sweet”. I can tell you have not come by a sweet pool, have you? Ok. Let’s find it source. Streams flows…. Pool forms…… but many times we forget their source. Let’s keep tracing the source.\n" +
                "look over there – Can you see it surging from the center…… Now, the source is not far way Here is it. Yes, its you NKEM Over there is the pool; right here is the source, cool, pure, sweet. Now you know why the pool is Sweet – it is the source that makes it sweet.\n" +
                "Nkem, there is more to show you in this heart of Mine. But I will continue another time.\"\n" +
                "  },\n" +
                "  \"speech2\": {\n" +
                "    \"length\": 100,\n" +
                "    \"text\": \"Nkem, see yourself through my eyes, she how beautiful you are. My dearest, sweetest, adorable darling. What can you see? Is that all you can see? There is more. Look into my heart what do you find? Take a stroll and tell me what you see. What can you see? Thank you, but there is more, what else can you see? Cant you see “NKEM” everywhere in my heart. You did not mention it. Have another look- You take the center stage. See how priceless, esteemed and adorable you are. Look around, there is more\n" +
                "How can you find enough, when there is a lot more to show you? There is more – my NKEM in many beautiful colours Can you see the pool in my heart? Come let me show you around. Here is it. This pool is always cool, calm, soothing and “sweet”. I can tell you have not come by a sweet pool, have you? Ok. Let’s find it source. Streams flows…. Pool forms…… but many times we forget their source. Let’s keep tracing the source.\n" +
                "look over there – Can you see it surging from the center…… Now, the source is not far way Here is it. Yes, its you NKEM Over there is the pool; right here is the source, cool, pure, sweet. Now you know why the pool is Sweet – it is the source that makes it sweet.\n" +
                "Nkem, there is more to show you in this heart of Mine. But I will continue another time.\"\n" +
                "  }\n" +
                "}";


        Gson gson = new Gson();
        PackageTO packageTo = gson.fromJson(inputJson, PackageTO.class);
        // rawText = gson.fromJson(inputJson.toString(), RawText.class);

        TextStats text1 = parser.parse(packageTo.getSpeech1().getText(), packageTo.getSpeech1().getLength());
        TextStats text2 = parser.parse(packageTo.getSpeech2().getText(), packageTo.getSpeech1().getLength());


        Map<String, Double> contexts = algorithm.process(text1, text2);

        JsonObject jsonResponse = new JsonObject();
        JsonArray array = new JsonArray();
        for (Map.Entry<String, Double> entry : contexts.entrySet()) {
            JsonObject context = new JsonObject();
            context.addProperty("relationship_type", entry.getKey());
            context.addProperty("percent", entry.getValue());
            array.add(context);
        }
        jsonResponse.add("contexts", array);

        String response = jsonResponse.toString();
        System.out.println(response);
    }
}
