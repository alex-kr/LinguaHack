import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linguahack.app.algorithm.Algorithm;
import com.linguahack.app.core.Text;
import com.linguahack.app.parser.Parser;
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


public class Main extends HttpServlet {
    Parser parser;
    Algorithm algorithm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        showOverview(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        class RawText {
            String text1;
            String text2;
        }

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

        Text text1 = parser.parse(rawText.text1);
        Text text2 = parser.parse(rawText.text2);

        Map<String, Double> contexts = algorithm.process(text1, text2);


    }

    private void showOverview(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.getWriter().print("Hello! It's a LinguaHack app");
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

