package demo.resource;

import com.m3.multilane.HttpGetToStringMultiLane;
import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.scalaflavor4j.SMap;
import com.m3.scalaflavor4j.Tuple2;
import com.m3.scalaflavor4j.VoidF1;
import demo.view.TemplateEngineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Map;

@Path("/multilane")
public class MultiLaneResource {

    Logger log = LoggerFactory.getLogger(MultiLaneResource.class);

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    @Context
    ServletContext servletContext;

    @GET
    public Object multiLane(@QueryParam("timeout") @DefaultValue("5000") Integer timeout) {

        Long start = System.currentTimeMillis();

        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();

        HttpGetToStringAction wait1sec = new HttpGetToStringAction("http://localhost:8080/api/1000", timeout);
        HttpGetToStringAction wait2sec = new HttpGetToStringAction("http://localhost:8080/api/2000", timeout);
        HttpGetToStringAction wait3sec = new HttpGetToStringAction("http://localhost:8080/api/3000", timeout);

        String unavailable = "<li>Unavailable</li>";

        multiLane.start("p1", wait1sec, unavailable);
        multiLane.start("p2", wait1sec, unavailable);
        multiLane.start("p3", wait1sec, unavailable);
        multiLane.start("p4", wait2sec, unavailable);
        multiLane.start("p5", wait2sec, unavailable);
        multiLane.start("p6", wait3sec, unavailable);

        Map<String, String> parts = multiLane.collectValues();

        log.info("*** Spent time for each part ***");
        for (Map.Entry<String, Long> spentTime : multiLane.spentTime().entrySet()) {
            log.info(" " + spentTime.getKey() + " -> " + spentTime.getValue() + " millis.");
        }
        log.info("********************************");

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("p1", parts.get("p1"));
        context.setVariable("p2", parts.get("p2"));
        context.setVariable("p3", parts.get("p3"));
        context.setVariable("p4", parts.get("p4"));
        context.setVariable("p5", parts.get("p5"));
        context.setVariable("p6", parts.get("p6"));
        context.setVariable("spentTime", System.currentTimeMillis() - start);

        TemplateEngine engine = TemplateEngineManager.getTemplateEngine();
        return Response.ok(engine.process("parts", context)).build();
    }

}
