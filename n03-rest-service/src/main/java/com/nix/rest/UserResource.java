package com.nix.rest;

import com.nix.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface UserResource {

    @GET
    List<User> getAllUsers();

    @GET
    @Path("{login}")
    Response getUserByLogin(@PathParam("login") String login);

    @POST
    Response createUser(User newUser) throws Exception;

    @PUT
    @Path("{login}")
    Response updateUser(User updatedUser, @PathParam("login") String login);

    @DELETE
    @Path("{login}")
    Response deleteUser(@PathParam("login") String login);
}
