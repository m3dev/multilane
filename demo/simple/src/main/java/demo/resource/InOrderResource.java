package demo.resource;

import demo.view.TemplateEngineManager;
import com.sun.jersey.api.client.Client;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/in-order")
public class InOrderResource {

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    @Context
    ServletContext servletContext;

    private String get(String url, Integer timeout) throws Exception {
        try {
            Client client = Client.create();
            client.setReadTimeout(timeout);
            return client.resource(url).get(String.class);
        } catch (Exception e) {
            return "<li>Unavailable</li>";

        }
    }

    @GET
    public Object inOrder(@QueryParam("timeout") @DefaultValue("5000") Integer timeout) throws Exception {

        Long start = System.currentTimeMillis();

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("p1", get("http://localhost:8080/api/1000", timeout));
        context.setVariable("p2", get("http://localhost:8080/api/1000", timeout));
        context.setVariable("p3", get("http://localhost:8080/api/1000", timeout));
        context.setVariable("p4", get("http://localhost:8080/api/2000", timeout));
        context.setVariable("p5", get("http://localhost:8080/api/2000", timeout));
        context.setVariable("p6", get("http://localhost:8080/api/3000", timeout));
        context.setVariable("spentTime", System.currentTimeMillis() - start);

        TemplateEngine engine = TemplateEngineManager.getTemplateEngine();
        return Response.ok(engine.process("parts", context)).build();
    }

}
