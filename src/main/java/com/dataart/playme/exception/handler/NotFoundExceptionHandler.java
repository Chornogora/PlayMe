package com.dataart.playme.exception.handler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/exception/404")
public class NotFoundExceptionHandler extends HttpServlet {

    private static final int NOT_FOUND_STATUS_CODE = 404;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(NOT_FOUND_STATUS_CODE);
    }
}
