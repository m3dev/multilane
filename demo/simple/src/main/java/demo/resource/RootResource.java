package demo.resource;

import demo.view.TemplateEngineManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    @Context
    ServletContext servletContext;

    @GET
    public Object index() {
        TemplateEngine engine = TemplateEngineManager.getTemplateEngine();
        WebContext context = new WebContext(request, response, servletContext);
        return Response.ok(engine.process("index", context)).build();
    }

}
