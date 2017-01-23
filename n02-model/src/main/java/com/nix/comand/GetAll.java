package com.nix.comand;

import com.nix.dao.RoleDao;
import com.nix.dao.UserDao;
import com.nix.model.Role;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "rrl", name = "getAll")
public class GetAll implements Action {

    private RoleDao roleDao;

    private UserDao userDao;

    public void setRoleDao(RoleDao roleDao) {
        System.out.println("set roleDao");

        if (roleDao == null) {
            System.out.println("roleDao is null");
        }

        this.roleDao = roleDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {

        Role r = roleDao.findById(1L);

        if (r == null) {
            System.out.println("role not found");
        } else {

            System.out.println(r.getId() + " | " + r.getName());

            System.out.println("command get all up");

            System.out.println(userDao.findAll());
        }
        return null;
    }
}
