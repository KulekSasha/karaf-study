package com.nix.dao;

import com.nix.model.User;

import java.util.List;

public interface UserDao {

    void create(User user);

    User update(User user);

    void remove(User user);

    List<User> findAll();

    User findByLogin(String login);

    User findByEmail(String email);

}
