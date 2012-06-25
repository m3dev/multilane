package demo.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Date;

@Path("/api")
public class ApiResource {

    public String time() {
        return "<li>(" + new Date().toString() + ")</li>";
    }

    @GET
    @Path("/headline")
    public String headline() throws Exception {
        Thread.sleep(200L);
        return "<li><a href=\"#\">Wikipedia founder steps in to help UK hacker</a></li>"
                + "<li><a href=\"#\">Why Your Links Should Never Say “Click Here” </a></li>"
                + "<li><a href=\"#\">Udacity aims to teach 160,000+ students statistics</a></li>" + time();
    }

    @GET
    @Path("/weather")
    public String weather(@QueryParam("area") String area) throws Exception {
        Thread.sleep(300L);
        if (area.equals("Tokyo")) {
            return "<li>Sunshine. Highs in the mid 80s and lows in the low 70s.</li>" + time();
        } else if (area.equals("Kyoto")) {
            return "<li>Times of sun and clouds. Highs in the low 80s and lows in the mid 60s.</li>" + time();
        } else {
            return "<li>Unknown</li>" + time();
        }
    }

    @GET
    @Path("/mail")
    public String mail(@QueryParam("userId") String userId) throws Exception {
        Thread.sleep(400L);
        return "<li><a href=\"#\">Hello, " + userId + "! ...</a></li>"
                + "<li><a href=\"#\">Your Google Play Order Receipt from ...</a></li>"
                + "<li><a href=\"#\">Announcing something new on AWS ...</a></li>" + time();
    }

    @GET
    @Path("/recommends")
    public String recommends(@QueryParam("userId") String userId) throws Exception {
        Thread.sleep(500L);
        return "<li><a href=\"#\">111" + userId + userId + userId + userId + "</a></li>"
                + "<li><a href=\"#\">222" + userId + userId + userId + userId + "</a></li>"
                + "<li><a href=\"#\">333" + userId + userId + userId + userId + "</a></li>" + time();
    }

}
