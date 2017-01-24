package com.nix.rest.impl;

import com.nix.dao.UserDao;
import com.nix.model.User;
import com.nix.rest.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

public class UserResourceImpl implements UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResourceImpl.class);

    private UserDao userDao;

    @Context
    private UriInfo uriInfo;

    public void setUserDao(UserDao userDao) {
        log.debug("set up userDao");
        this.userDao = userDao;
    }

    public void setUriInfo(UriInfo uriInfo) {
        log.debug("set up uriInfo");
        this.uriInfo = uriInfo;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("requesting all users");
        return userDao.findAll();
    }

    @Override
    public Response getUserByLogin(String login) {
        System.out.println("login: " + login);
        log.debug("requesting user by login: {}", login);
        User user = userDao.findByLogin(login);
        log.debug("found user: {}", user);

        return user != null
                ? Response.ok(user).build()
                : Response.status(404).entity("").build();
    }

    @Override
    public Response createUser(User newUser) throws Exception{
        log.debug("create user, incoming user: {}", newUser);

        if (newUser == null) {
            return Response.status(400).entity("").type(MediaType.APPLICATION_JSON).build();
        }

        log.debug("create new user: {}", newUser);
        userDao.create(newUser);


        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(newUser.getLogin())
                .build();
        if (uri == null){
            uri = new URI("users/" + newUser.getLogin());
        }

        return Response
                .created(uri)
                .entity(userDao.findByLogin(newUser.getLogin()))
                .build();
    }

    @Override
    public Response updateUser(User updatedUser, String login) {
        log.debug("update user, incoming user: {}", updatedUser);

        User currentUser = userDao.findByLogin(login);

        if (currentUser == null) {
            return Response.status(404).entity("").build();
        }

        log.debug("update user: {}", updatedUser);
        userDao.update(updatedUser);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(updatedUser.getLogin())
                .build();

        return Response
                .ok()
                .entity(updatedUser)
                .type(MediaType.APPLICATION_JSON)
                .header("Location", uri)
                .build();
    }

    @Override
    public Response deleteUser(String login) {
        log.debug("delete user, incoming login: {}", login);
        User userToRemove = userDao.findByLogin(login);

        if (userToRemove == null) {
            log.debug("user not found");
            return Response.status(404).entity("").build();
        }

        log.debug("remove user: {}", userToRemove);
        userDao.remove(userToRemove);

        return Response
                .ok()
                .build();
    }
}
