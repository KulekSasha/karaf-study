package com.nix.dao;

import com.nix.model.Role;

import java.util.List;

public interface RoleDao {

    void create(Role role);

    void update(Role role);

    void remove(Role role);

    List<Role> findAll();

    Role findByName(String name);

    Role findById(long id);

}
