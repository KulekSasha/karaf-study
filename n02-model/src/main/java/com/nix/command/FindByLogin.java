package com.nix.command;

import com.nix.dao.RoleDao;
import com.nix.dao.UserDao;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Command(scope = "user-app", name = "by-login")
@Service
public class FindByLogin implements Action {

    @Reference
    private RoleDao roleDao;
    @Reference
    private UserDao userDao;

    @Override
    public Object execute() throws Exception {
        System.out.println("find by login: testUser_4");
        System.out.println(userDao.findByLogin("testUser_4"));
        return null;
    }
}
