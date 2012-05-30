package com.m3.multilane.demo.resource;

import com.m3.multilane.action.HttpGetAction;
import com.m3.multilane.HttpGetMultiLane;
import com.m3.multilane.demo.view.ViewModel;
import com.m3.scalaflavor4j.SMap;
import com.m3.scalaflavor4j.Tuple2;
import com.m3.scalaflavor4j.VoidF1;
import com.sun.jersey.api.view.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/multilane")
public class MultiLaneResource {

    Logger log = LoggerFactory.getLogger(MultiLaneResource.class);

    @GET
    public Object multiLane(@QueryParam("timeout") @DefaultValue("5000") Integer timeout) {

        Long start = System.currentTimeMillis();

        HttpGetMultiLane multiLane = new HttpGetMultiLane();

        HttpGetAction wait1sec = new HttpGetAction("http://localhost:8080/api/1000", "UTF-8", timeout);
        HttpGetAction wait2sec = new HttpGetAction("http://localhost:8080/api/2000", "UTF-8", timeout);
        HttpGetAction wait3sec = new HttpGetAction("http://localhost:8080/api/3000", "UTF-8", timeout);

        String unavailable = "<li>Unavailable</li>";

        multiLane.start("p1", wait1sec, unavailable);
        multiLane.start("p2", wait1sec, unavailable);
        multiLane.start("p3", wait1sec, unavailable);
        multiLane.start("p4", wait2sec, unavailable);
        multiLane.start("p5", wait2sec, unavailable);
        multiLane.start("p6", wait3sec, unavailable);

        Map<String, String> parts = multiLane.collectValues();

        ViewModel model = new ViewModel();
        model.setP1(parts.get("p1"));
        model.setP2(parts.get("p2"));
        model.setP3(parts.get("p3"));
        model.setP4(parts.get("p4"));
        model.setP5(parts.get("p5"));
        model.setP6(parts.get("p6"));
        model.setSpentTime(System.currentTimeMillis() - start);

        SMap._(multiLane.spentTime()).foreach(new VoidF1<Tuple2<String, Long>>() {
            public void _(Tuple2<String, Long> each) {
                log.info(each._1() + " -> " + each._2() + " ms");
            }
        });

        return Response.ok(new Viewable("/parts.jsp", model)).build();
    }

}
