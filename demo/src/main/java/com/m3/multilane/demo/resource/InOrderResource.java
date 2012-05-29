package com.m3.multilane.demo.resource;

import com.m3.multilane.demo.view.ViewModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.view.Viewable;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/in-order")
public class InOrderResource {

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

        ViewModel model = new ViewModel();
        model.setP1(get("http://localhost:8080/api/1000", timeout));
        model.setP2(get("http://localhost:8080/api/1000", timeout));
        model.setP3(get("http://localhost:8080/api/1000", timeout));
        model.setP4(get("http://localhost:8080/api/2000", timeout));
        model.setP5(get("http://localhost:8080/api/2000", timeout));
        model.setP6(get("http://localhost:8080/api/3000", timeout));
        model.setSpentTime(System.currentTimeMillis() - start);

        return Response.ok(new Viewable("/parts.jsp", model)).build();
    }

}
