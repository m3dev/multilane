package demo.resource;

import demo.aggregator.*;
import demo.view.TemplateEngineManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/")
public class RootResource {

    @Context
    HttpServletRequest request;
    @Context
    ServletContext servletContext;

    @GET
    public Object index(
            @QueryParam("userId") @DefaultValue("Alice") String userId,
            @QueryParam("timeout") Integer timeout) {

        User user = DataStore.findUser(userId);
        AggregatorSetting aggregatorSetting = new AggregatorSetting(user);

        List<ContentSetting> contentSettings = DataStore.findAllContentSettings();
        for (ContentSetting setting : contentSettings) {
            if (timeout != null) {
                // force timeout value
                setting.setTimeoutMilliseconds(timeout);
            }
        }

        Aggregator aggregator = new Aggregator(aggregatorSetting, contentSettings).start();

        // do something other...

        // blocking!
        Map<String, String> contents = aggregator.collectValues();

        // bind contents
        WebContext context = new WebContext(request, servletContext);
        for (ContentSetting setting : contentSettings) {
            context.setVariable(setting.getId(), contents.get(setting.getId()));
        }
        TemplateEngine engine = TemplateEngineManager.getTemplateEngine();
        return Response.ok(engine.process("index", context)).build();
    }

}
