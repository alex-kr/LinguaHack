import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import com.google.gson.Gson;
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
    Gson gson = new Gson();
    StringBuilder sb = new StringBuilder();
    String s;
    while ((s = req.getReader().readLine()) != null) {
      sb.append(s);
    }

    DataSetTo dataTo = (DataSetTo) gson.fromJson(sb.toString(), DataSetTo.class);

    resp.getWriter().print("Hello! Your object: " + dataTo.toString());

  }

  private void showHome(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    HashMap<Long, List<Long>> map = Test.testIt();
    Gson gson = new Gson();
    String result = gson.toJson(map);
    resp.getWriter().print(result);
  }


  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
