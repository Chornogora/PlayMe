package com.dataart.playme.exception.handler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

@WebServlet(urlPatterns = "/exception/500")
public class InternalServerErrorHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}
