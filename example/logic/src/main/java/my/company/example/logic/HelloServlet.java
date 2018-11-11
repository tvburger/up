package my.company.example.logic;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    private String message;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        message = servletConfig.getInitParameter("message");
        if (message == null) {
            throw new ServletException("No message init parameter defined!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getOutputStream().print(message);
        resp.getOutputStream().flush();
    }

}
