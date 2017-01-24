package com.nix.dao.impl;

import com.nix.dao.UserDao;
import com.nix.model.Role;
import com.nix.model.User;

import javax.persistence.EntityManager;
import java.util.List;

public class UserDaoHibernate implements UserDao {

    private static final String FIND_ALL_QUERY = "select u from User u";
    private static final String FIND_BY_LOGIN = "select u from User u where lower(u.login)=:login";
    private static final String FIND_BY_EMAIL = "select u from User u where lower(u.email)=:email";

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public User update(User user) {
        User result = entityManager.merge(user);
        entityManager.flush();
        return result;
    }

    @Override
    public void remove(User user) {
        User persistedUser = entityManager.find(User.class, user.getId());

        if (persistedUser != null) {
            entityManager.remove(persistedUser);
            entityManager.flush();
        }
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, User.class)
                .getResultList();
    }

    @Override
    public User findByLogin(String login) {
        List<User> users = entityManager.createQuery(FIND_BY_LOGIN, User.class)
                .setParameter("login", login)
                .getResultList();

        return (users != null)
                ? (users.isEmpty() ? null : users.get(0))
                : null;
    }

    @Override
    public User findByEmail(String email) {
        List<User> users = entityManager.createQuery(FIND_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();

        return (users != null)
                ? (users.isEmpty() ? null : users.get(0))
                : null;
    }
}
