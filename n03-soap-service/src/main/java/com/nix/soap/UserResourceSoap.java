package com.nix.soap;

import com.nix.model.User;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface UserResourceSoap {

    @WebMethod
    @WebResult(name = "user")
    List<User> getAllUsers();

    @WebMethod
    @WebResult(name = "user")
    User getUserByLogin(String login);

    @WebMethod
    @WebResult(name = "user")
    User createUser(User newUser) throws Exception;

    @WebMethod
    @WebResult(name = "user")
    User updateUser(User updatedUser, String login);

    @WebMethod
    void deleteUser(String login);
}
