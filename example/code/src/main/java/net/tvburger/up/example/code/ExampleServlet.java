package net.tvburger.up.example.code;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExampleServlet extends HttpServlet {

    private final ExampleService exampleService;

    public ExampleServlet(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().print(exampleService.sayHelloTo(req.getPathInfo()));
        resp.getOutputStream().flush();
    }

}
