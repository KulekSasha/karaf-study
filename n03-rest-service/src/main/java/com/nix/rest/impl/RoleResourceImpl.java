package com.nix.rest.impl;

import com.nix.dao.RoleDao;
import com.nix.model.Role;
import com.nix.rest.RoleResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public class RoleResourceImpl implements RoleResource {

    private static final Logger log = LoggerFactory.getLogger(RoleResourceImpl.class);

    private RoleDao roleDao;

    @Context
    private UriInfo uriInfo;

    public void setRoleDao(RoleDao roleDao) {
        log.debug("set up roleDao");
        this.roleDao = roleDao;
    }

    public void setUriInfo(UriInfo uriInfo) {
        log.debug("set up uriInfo");
        this.uriInfo = uriInfo;
    }

    @Override
    public List<Role> getAllRoles() {
        log.debug("requesting all roles");
        return roleDao.findAll();
    }

    @Override
    public Response getRoleByName(String name) {
        log.debug("requesting role by name: {}", name);
        Role role = roleDao.findByName(name);
        log.debug("found role: {}", role);

        return role != null
                ? Response.ok(role).build()
                : Response.status(404).entity("").build();
    }

}
