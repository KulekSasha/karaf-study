package com.nix.soap;

import com.nix.model.Role;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService(name = "roleService")
public interface RoleResourceSoap {

    @WebMethod
    @WebResult(name = "role")
    List<Role> getAllRoles();

    @WebMethod
    @WebResult(name = "user")
    Role getRoleByName(String roleName);

}
