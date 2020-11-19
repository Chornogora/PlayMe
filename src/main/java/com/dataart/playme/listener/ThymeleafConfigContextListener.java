package com.dataart.playme.listener;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafConfigContextListener implements ServletContextListener {

    private static final String TEMPLATES_PATH = "/WEB-INF/templates/";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ITemplateResolver templateResolver = getTemplateResolver(servletContext);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        servletContext.setAttribute(TemplateEngine.class.getName(), engine);
    }

    private ITemplateResolver getTemplateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix(TEMPLATES_PATH);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}
