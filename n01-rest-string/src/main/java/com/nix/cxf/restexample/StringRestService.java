package com.nix.cxf.restexample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public interface StringRestService {

    @GET
    @Path("/rest_001")
    @Produces("application/json")
    public String getString() throws Exception;
}