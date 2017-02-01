package com.nix.command;

import com.nix.dao.RoleDao;
import com.nix.dao.UserDao;
import com.nix.model.Role;
import com.nix.model.User;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import java.util.List;

@Command(scope = "user-app", name = "get-all")
@Service
public class GetAll implements Action {

    @Reference
    private RoleDao roleDao;

    @Reference
    private UserDao userDao;

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Object execute() throws Exception {

        Role role = roleDao.findById(1L);

        if (role != null) {
            System.out.println("role with id 1: " + role);
        }

        System.out.println("All users:");
        List<User> users = userDao.findAll();
        if (users != null) {
            users.forEach(System.out::println);
        }

        System.out.println("\nfind by login - testUser_4:");
        System.out.println(userDao.findByLogin("testUser_4"));

        return null;
    }
}
