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
                "    \"text\": \"See yourself through my eyes, she how beautiful you are.\n" +
                "My dearest, sweetest, adorable darling. What can you see?\n" +
                "Is that all you can see?\n" +
                "There is more. Look into my heart what do you find? Take a stroll and tell me what you see.\n" +
                "What can you see?\n" +
                "Thank you, but there is more, what else can you see? Cant you see  everywhere in my heart. You did not mention it.\n" +
                "Have another look- You take the center stage.\n" +
                "See how priceless, esteemed and adorable you are.\n" +
                "Look around, there is more\n" +
                "How can you find enough, when there is a lot more to show you? There is more in many beautiful colors\n" +
                "Can you see the pool in my heart? Come let me show you around.\n" +
                "Here is it. This pool is always cool, calm, soothing and “sweet”. I can tell you have not come by a sweet pool, have you?\n" +
                "Ok. Let’s find it source. Streams flows…. Pool forms……\n" +
                "but many times we forget their source. Let’s keep tracing the source.\n" +
                "look over there – Can you see it surging from the center……\n" +
                "Now, the source is not far way\n" +
                "Here is it.\n" +
                "Yes, its you \n" +
                "Over there is the pool; right here is the source, cool, pure, sweet. Now you know why the pool is Sweet – it is the source that makes it sweet.\n" +
                "There is more to show you in this heart of Mine. But I will continue another time.\"\n" +
                "  },\n" +
                "  \"speech2\": {\n" +
                "    \"length\": 100,\n" +
                "    \"text\": \"Ok. I am doing that right now\n" +
                "I see beauty that takes it roots from Love\n" +
                "That’s what I see.\n" +
                "I will look everywhere as long as it is your heart.\n" +
                "I see purity, honesty and Faithfulness. That’s what I see and know about you for years now.\n" +
                "Smiles\n" +
                "It gladdens my heart to know that.\n" +
                "What could that be? For me, I have found enough.\n" +
                "Thank you dear\n" +
                "Ok. I can’t wait to see it.\n" +
                "No I have not.\n" +
                "Yes, sure\n" +
                "ok\n" +
                "Smiles\n" +
                "ok\n" +
                "You have found it?\n" +
                "Oh! What a writer you have become. So creative. I love You\n" +
                "I have had a great time with you dear mi, sweet night dear. I Love you\"\n" +
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
