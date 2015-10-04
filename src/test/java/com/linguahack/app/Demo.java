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
                "    \"length\": 1000,\n" +
                "    \"text\": \"Hello! How r u! Great thx, meeting 2day?\"\n" +
                "  },\n" +
                "  \"speech2\": {\n" +
                "    \"length\": 1000,\n" +
                "    \"text\": \"Hello! How r u! Great thx, meeting 2day?\"\n" +
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
