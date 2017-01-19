package com.nix.model;

import java.util.List;

public interface UserDao {

    long create(User user);

    User update(User user);

    long remove(User user);

    List<User> findAll();

    User findByLogin(String login);

    User findByEmail(String email);

}
