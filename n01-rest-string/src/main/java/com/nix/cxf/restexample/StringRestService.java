package com.nix.cxf.restexample;

import com.nix.model.Role;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/")
public interface StringRestService {

    @GET
    @Path("/rest_001")
    @Produces("application/json")
    public String getString() throws Exception;


    @GET
    @Path("/rest_002")
    @Produces("application/json")
    public Role getString2() throws Exception;
}