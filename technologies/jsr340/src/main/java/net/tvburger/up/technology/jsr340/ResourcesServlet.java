package net.tvburger.up.technology.jsr340;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public final class ResourcesServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ResourcesServlet.class.getName());

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getInitParameter("resourcePath");
        if (path == null || path.isEmpty()) {
            throw new ServletException();
        }
        String resourcePath = stripBeginAndEndSlash(path) + "/" + stripBeginSlash(req.getPathInfo());
        logger.info("Serving file: " + resourcePath);
        InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        byte[] buffer = new byte[1024 * 1024];
        for (int i = in.read(buffer); i >= 0; i = in.read(buffer)) {
            resp.getOutputStream().write(buffer, 0, i);
        }
        resp.flushBuffer();
    }

    private String stripBeginAndEndSlash(String path) {
        String strippedBegin = stripBeginSlash(path);
        return strippedBegin.endsWith("/") ? path.substring(0, strippedBegin.length() - 1) : strippedBegin;
    }

    private String stripBeginSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }

}