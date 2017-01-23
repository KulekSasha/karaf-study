package com.nix.cxf.restexample;

import com.nix.dao.RoleDao;
import com.nix.model.Role;

import java.util.ArrayList;
import java.util.List;

public class StringRestServiceImpl implements StringRestService {

    //   @Inject @OsgiService(filter = "osgi.jndi.service.name=jdbc/h2ds")
//    DataSource dataSource;
//
//    public void setDataSource(DataSource dataSource) {
//        System.out.println("set DS");
//        this.dataSource = dataSource;
//    }


    RoleDao roleDao;

    public void setRoleDao(RoleDao roleDao) {
        System.out.println("set RoleDao");
        this.roleDao = roleDao;
    }


    @Override
    public String getString() throws Exception {
        return "test stringss - " + System.currentTimeMillis();
    }

    @Override
    public Role getString2() throws Exception {

//        List<String> res = new ArrayList<>();

//        if (dataSource != null) {
//            Connection connection = dataSource.getConnection();
//
//            Statement statement = connection.createStatement();
//
//            ResultSet rs = statement.executeQuery("select * FROM PERSON_ROLE ");
//
//            while (rs.next()) {
//                String name = rs.getString("NAME");
//                System.out.println(name);
//                res.add(name);
//            }
//        } else {
//            System.out.println("ds is null");
//        }

        return roleDao.findById(1L);
    }
}
