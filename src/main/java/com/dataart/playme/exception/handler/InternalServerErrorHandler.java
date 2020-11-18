package com.dataart.playme.exception.handler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/exception/500")
public class InternalServerErrorHandler extends HttpServlet {

    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(INTERNAL_SERVER_ERROR_STATUS_CODE);
    }
}
