package com.nix.dao.impl;

import com.nix.dao.UserDao;
import com.nix.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class UserDaoHibernate implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoHibernate.class);

    private static final String FIND_ALL_QUERY = "select u from User u";
    private static final String FIND_BY_LOGIN_QUERY = "select u from User u where lower(u.login)=:login";
    private static final String FIND_BY_EMAIL_QUERY = "select u from User u where lower(u.email)=:email";

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(User user) {
        log.debug("invoke create with param: {}", user);
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void update(User user) {
        log.debug("invoke update with param: {}", user);
        entityManager.merge(user);
        entityManager.flush();
    }

    @Override
    public void remove(User user) {
        log.debug("invoke remove with param: {}", user);
        User persistedUser = entityManager.find(User.class, user.getId());

        if (persistedUser != null) {
            entityManager.remove(persistedUser);
            entityManager.flush();
        }
    }

    @Override
    public List<User> findAll() {
        log.debug("invoke findAll");
        return entityManager.createQuery(FIND_ALL_QUERY, User.class)
                .getResultList();
    }

    @Override
    public User findByLogin(String login) {

        if (login == null) {
            return null;
        }

        log.debug("invoke findByLogin with param: {}", login);
        List<User> users = entityManager.createQuery(FIND_BY_LOGIN_QUERY, User.class)
                .setParameter("login", login.toLowerCase())
                .getResultList();
        log.debug("result list: {}", users);

        return (users != null)
                ? (users.isEmpty() ? null : users.get(0))
                : null;
    }

    @Override
    public User findByEmail(String email) {

        if (email == null) {
            return null;
        }

        log.debug("invoke findByEmail with param: {}", email);
        List<User> users = entityManager.createQuery(FIND_BY_EMAIL_QUERY, User.class)
                .setParameter("email", email.toLowerCase())
                .getResultList();
        log.debug("result list: {}", users);

        return (users != null)
                ? (users.isEmpty() ? null : users.get(0))
                : null;
    }
}
