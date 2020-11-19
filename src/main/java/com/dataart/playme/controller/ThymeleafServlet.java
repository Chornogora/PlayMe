package com.dataart.playme.controller;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public abstract class ThymeleafServlet extends HttpServlet {

    protected TemplateEngine templateEngine;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        templateEngine = (TemplateEngine) servletContext.getAttribute(TemplateEngine.class.getName());
    }
}
