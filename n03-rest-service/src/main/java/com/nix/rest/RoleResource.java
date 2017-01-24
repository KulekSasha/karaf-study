package com.nix.rest;

import com.nix.model.Role;
import com.nix.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/roles")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface RoleResource {

    @GET
    List<Role> getAllRoles();

    @GET
    @Path("{roleName}")
    Response getRoleByName(@PathParam("roleName") String roleName);
}
