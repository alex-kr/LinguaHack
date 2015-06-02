import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import CLOPE.CLOPEClusterer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import com.google.gson.Gson;
import to.ClusterTO;
import to.DataSetTo;

import java.util.HashMap;
import java.util.List;

public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

      showHome(req,resp);

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    resp.setContentType("application/json");
    Gson gson = new Gson();
    StringBuilder sb = new StringBuilder();
    String s;
    while ((s = req.getReader().readLine()) != null) {
      sb.append(s);
    }
    DataSetTo dataTo = (DataSetTo) gson.fromJson(sb.toString(), DataSetTo.class);
    CLOPEClusterer clusterer = new CLOPEClusterer();
    clusterer.setRepulsion(dataTo.getRepulsion());
    clusterer.setMinClusterSize(dataTo.getMinSize());
    List<ClusterTO> clusters = null;
    try {
      clusters = clusterer.buildClusterer(dataTo.getData());
    } catch (Exception e) {
      e.printStackTrace();
    }
    String result = gson.toJson(clusters);
    resp.getWriter().print(result);

  }

  private void showHome(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.getWriter().print("Hello! It's a CLOPE clustering app\n Input example:\n" +
            "{\n" +
            "  \"repulsion\": 2,\n" +
            "  \"minSize\": 1,\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"items\": [5, 6]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"items\": [7, 5, 38]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 17,\n" +
            "      \"items\": [37, 53, 6,8]\n" +
            "    },\n" +
            "{\n" +
            "      \"id\": 15,\n" +
            "      \"items\": [37, 53, 6]\n" +
            "    }\n" +
            "  ]\n" +
            "}" + "\n Output example: " +
            "[{\"clusterId\":7,\"items\":[1]},{\"clusterId\":8,\"items\":[2]},{\"clusterId\":9,\"items\":[17,15]}]");
  }


  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
