package demo.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Date;

@Path("/api")
public class ApiResource {

    @GET
    @Path("/1000")
    public String wait1000() throws Exception {
        Thread.sleep(1000L);
        return "<li>A part responded in 1000 millis (" + new Date() + ")</li>";
    }

    @GET
    @Path("/2000")
    public String wait2000() throws Exception {
        Thread.sleep(2000L);
        return "<li>A part responded in 2000 millis (" + new Date() + ")</li>";
    }

    @GET
    @Path("/3000")
    public String wait3000() throws Exception {
        Thread.sleep(3000L);
        return "<li>A part responded in 3000 millis (" + new Date() + ")</li>";
    }

}
