package com.nix.comand;

import com.nix.dao.RoleDao;
import com.nix.dao.UserDao;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "user-app", name = "by-login")
public class FindByLogin implements Action {

    private RoleDao roleDao;

    private UserDao userDao;

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        System.out.println("find by login: testUser_4");
        System.out.println(userDao.findByLogin("testUser_4"));
        return null;
    }
}
