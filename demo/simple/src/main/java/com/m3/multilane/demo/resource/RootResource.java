package com.m3.multilane.demo.resource;

import com.m3.multilane.demo.view.TemplateEngineManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @Context
    HttpServletRequest request;
    @Context
    ServletContext servletContext;

    @GET
    public Object index() {
        TemplateEngine engine = TemplateEngineManager.getTemplateEngine();
        WebContext context = new WebContext(request, servletContext);
        return Response.ok(engine.process("index", context)).build();
    }

}
