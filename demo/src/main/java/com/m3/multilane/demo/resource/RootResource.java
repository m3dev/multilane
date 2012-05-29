package com.m3.multilane.demo.resource;

import com.sun.jersey.api.view.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @GET
    public Object index() {
        return Response.ok(new Viewable("/index.jsp")).build();
    }

}
