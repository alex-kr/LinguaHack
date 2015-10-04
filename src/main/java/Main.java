import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.linguahack.app.algorithm.Algorithm;
import com.linguahack.app.algorithm.AlgorithmMockImpl;
import com.linguahack.app.algorithm.AlgorithmsBaseImpl;
import com.linguahack.app.core.TextStats;
import com.linguahack.app.parser.Parser;
import com.linguahack.app.parser.ParserImpl;
import com.linguahack.app.parser.ParserMockImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;


public class Main extends HttpServlet {
    Parser parser = new ParserImpl();
    Algorithm algorithm = new AlgorithmsBaseImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        showOverview(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        BufferedReader input = req.getReader();
        StringBuilder inputJson = new StringBuilder();
        try {
            String s;
            while ((s = input.readLine()) != null) {
                inputJson.append(s);
            }
        } catch (IOException ignored) {
            resp.sendError(400);
        }

        Gson gson = new GsonBuilder().create();

        RawText rawText = gson.fromJson(inputJson.toString(), RawText.class);

        if (rawText.text1 == null) {
            throw new IOException();
        }
        TextStats text1 = parser.parse(rawText.text1, 10);
        TextStats text2 = parser.parse(rawText.text2, 15);


        Map<String, Double> contexts = algorithm.process(text1, text2);

        JsonObject jsonResponse = new JsonObject();
        JsonArray array = new JsonArray();
        for (Entry<String, Double> entry : contexts.entrySet()) {
            JsonObject context = new JsonObject();
            context.addProperty("relationship_type", entry.getKey());
            context.addProperty("percent", entry.getValue());
            array.add(context);
        }
        jsonResponse.add("contexts", array);

        String response = jsonResponse.toString();
        resp.getWriter().print(response);
    }

    private void showOverview(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.getWriter().print("Hello! It's a LinguaHack app");
    }

    private class RawText {
        String text1;
        String text2;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Main()), "/*");
        server.start();
        server.join();
    }

}

