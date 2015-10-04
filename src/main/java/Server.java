import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.linguahack.app.algorithm.Algorithm;
import com.linguahack.app.algorithm.AlgorithmsBaseImpl;
import com.linguahack.app.core.TextStats;
import com.linguahack.app.parser.Parser;
import com.linguahack.app.parser.ParserImpl;
import com.linguahack.app.to.PackageTO;
import io.datakernel.async.ResultCallback;
import io.datakernel.eventloop.NioEventloop;
import io.datakernel.http.AsyncHttpServer;
import io.datakernel.http.HttpRequest;
import io.datakernel.http.HttpResponse;
import io.datakernel.http.server.AsyncHttpServlet;

import java.util.Map;

import static io.datakernel.util.ByteBufStrings.decodeAscii;
import static io.datakernel.util.ByteBufStrings.encodeAscii;

public class Server {
    public static final int PORT = 5588;
    private static final Parser parser = new ParserImpl();
    private static final Algorithm algorithm = new AlgorithmsBaseImpl();

    public static AsyncHttpServer server(NioEventloop primaryEventloop, int port) {
        AsyncHttpServer httpServer = new AsyncHttpServer(primaryEventloop, new AsyncHttpServlet() {
            @Override
            public void serveAsync(HttpRequest request, ResultCallback<HttpResponse> callback) {
                String inputJson = decodeAscii(request.getBody());

                Gson gson = new Gson();
                PackageTO packageTo = (PackageTO) gson.fromJson(inputJson, PackageTO.class);

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
                String outputJson = jsonResponse.toString();
                HttpResponse response = HttpResponse.create(200).body(encodeAscii(outputJson));
                callback.onResult(response);
            }
        });
        return httpServer.setListenPort(port);
    }


    public static void main(String[] args) throws Exception {
        final NioEventloop primaryEventloop = new NioEventloop();
        final AsyncHttpServer httpServerListener = server(primaryEventloop, PORT);

        System.out.println("Started HelloWorld HTTP Server on http://localhost:" + PORT);
        httpServerListener.listen();

        primaryEventloop.run();
    }


}
