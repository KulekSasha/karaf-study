package com.nix.soap.impl;

import com.nix.dao.RoleDao;
import com.nix.model.Role;
import com.nix.soap.RoleResourceSoap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import java.util.List;

@WebService(endpointInterface = "com.nix.soap.RoleResourceSoap")
public class RoleResourceSoapImpl implements RoleResourceSoap {

    private static final Logger log = LoggerFactory.getLogger(RoleResourceSoapImpl.class);

    private RoleDao roleDao;

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRoles() {
        log.debug("invoke getAllRoles");
        return roleDao.findAll();
    }

    @Override
    public Role getRoleByName(String roleName) {
        log.debug("invoke getRoleByName with name: {}", roleName);
        Role role = roleDao.findByName(roleName);
        if (role != null) {
            return role;
        } else {
            throw new WebServiceException("role [" + roleName + "] not found");
        }
    }
}
