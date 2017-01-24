package com.nix.dao.impl;

import com.nix.dao.RoleDao;
import com.nix.model.Role;
import com.nix.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class RoleDaoHibernate implements RoleDao {

    private static final Logger log = LoggerFactory.getLogger(RoleDaoHibernate.class);

    private static final String FIND_ALL_QUERY = "select r from Role r";
    private static final String FIND_BY_NAME_QUERY = "select r from Role r where lower(r.name)=:name";

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Role role) {
        entityManager.persist(role);
        entityManager.flush();
    }

    @Override
    public void update(Role role) {
        entityManager.merge(role);
        entityManager.flush();
    }

    @Override
    public void remove(Role role) {
        Role persistedRole = entityManager.find(Role.class, role.getId());

        if (persistedRole != null) {
            entityManager.remove(persistedRole);
            entityManager.flush();
        }
    }

    @Override
    public List<Role> findAll() {
        log.debug("invoke findAll");
        return entityManager.createQuery(FIND_ALL_QUERY, Role.class)
                .getResultList();
    }

    @Override
    public Role findByName(String name) {
        List<Role> roles = entityManager.createQuery(FIND_BY_NAME_QUERY, Role.class)
                .setParameter("name", name)
                .getResultList();

        return (roles != null)
                ? (roles.isEmpty() ? null : roles.get(0))
                : null;
    }

    @Override
    public Role findById(long id) {
        return entityManager.find(Role.class, id);
    }

}
