package com.nix.comand;

import com.nix.dao.RoleDao;
import com.nix.dao.UserDao;
import com.nix.model.Role;
import com.nix.model.User;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

import java.util.List;

@Command(scope = "user-app", name = "get-all")
public class GetAll implements Action {

    private RoleDao roleDao;

    private UserDao userDao;

    public void setRoleDao(RoleDao roleDao) {
        if (roleDao == null) {
            System.out.println("roleDao is null");
        }
        this.roleDao = roleDao;
    }

    public void setUserDao(UserDao userDao) {
        if (userDao ==null){
            System.out.println("userDao is null");
        }
        this.userDao = userDao;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {

        Role role = roleDao.findById(1L);

        if (role != null) {
            System.out.println("role with id 1: " + role);
        }

        List<User> users = userDao.findAll();

        if (users != null){
            users.forEach(System.out::println);
        }

        System.out.println("find by login:");
        System.out.println(userDao.findByLogin("testUser_4"));

        return null;
    }
}
