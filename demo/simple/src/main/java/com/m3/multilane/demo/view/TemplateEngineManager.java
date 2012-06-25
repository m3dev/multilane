package com.m3.multilane.demo.view;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class TemplateEngineManager {

    private static final ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
    private static final TemplateEngine engine = new TemplateEngine();

    static {
        resolver.setTemplateMode("XHTML");
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(3600000L);
        engine.setTemplateResolver(resolver);
    }

    public static TemplateEngine getTemplateEngine() {
        return engine;
    }


}
