package com.nix.cxf.restexample;

import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@OsgiServiceProvider(classes = {StringRestService.class})
@Singleton
public class StringRestServiceImpl implements StringRestService {

   @Inject @OsgiService(filter = "osgi.jndi.service.name=jdbc/h2ds")
    DataSource dataSource;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getString() throws Exception {
        return "test stringss - " + System.currentTimeMillis();
    }

    @Override
    public List<String> getString2() throws Exception {

        List<String> res = new ArrayList<>();

        if (dataSource != null) {
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("select * FROM PERSON_ROLE ");

            while (rs.next()) {
                String name = rs.getString("NAME");
                System.out.println(name);
                res.add(name);
            }
        } else {
            System.out.println("ds is null");
        }


        return res;
    }
}
