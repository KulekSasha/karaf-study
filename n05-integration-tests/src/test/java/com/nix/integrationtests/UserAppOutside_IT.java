package com.nix.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.nix.model.Role;
import com.nix.model.User;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.spi.PaxExamRuntime;

import javax.ws.rs.core.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.nix.integrationtests.KarafBaseConfig.getConfiguration;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAppOutside_IT {

    private final static String ENDPOINT_ADDRESS = "http://localhost:8181/cxf/user-app/rest";
    private final static String USERS_ENDPOINT_ADDRESS = ENDPOINT_ADDRESS + "/users";
    private final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";
    private final static int AWAIT_TO_KARAF_INIT_SECONDS = 30;

    private static TestContainer container;
    private static ObjectMapper jsonMapper;
    private static WebClient client;


    @BeforeClass
    public static void startKaraf() throws Exception {
        ExamSystem system = PaxExamRuntime
                .createServerSystem(getConfiguration());
        container = PaxExamRuntime.createContainer(system);
        container.start();
        waitForWADL();

        jsonMapper = new ObjectMapper();
        jsonMapper.setTimeZone(TimeZone.getDefault());

        List<Object> providers = new ArrayList<>();
        JacksonJaxbJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();
        providers.add(jacksonJsonProvider);
        client = WebClient.create(USERS_ENDPOINT_ADDRESS, providers)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);
    }

    @AfterClass
    public static void stopKaraf() throws Exception {
        client.close();
        container.stop();
    }

    private static void waitForWADL() throws Exception {
        WebClient client = WebClient.create(WADL_ADDRESS);
        for (int i = 0; i < AWAIT_TO_KARAF_INIT_SECONDS; i++) {
            TimeUnit.SECONDS.sleep(1);
            Response response = null;
            try {
                response = client.get();
            } catch (Exception e) {
                //NO OP
            }
            if (response != null && response.getStatus() == 200) {
                return;
            }
        }
        throw new Exception("Web service is not available after " +
                AWAIT_TO_KARAF_INIT_SECONDS + " seconds.");
    }

    @Test(timeout = 15_000L)
    public void t010_getAllUsers() throws Exception {
        int expectedListSize = 5;

        Response output = client.path(USERS_ENDPOINT_ADDRESS)
                .get();

        List<User> list = jsonMapper.readValue(output.readEntity(String.class),
                TypeFactory.defaultInstance().constructCollectionType(List.class,
                        User.class));

        assertEquals("should return status 200", Response.Status.OK.getStatusCode(),
                output.getStatus());

        assertEquals("should contain 5 user", expectedListSize, list.size());

    }

    @Test(timeout = 15_000L)
    public void t020_getUserByLogin() throws Exception {
        String userLogin = "testUser_1";

        User expectedUser = User.builder()
                .id(1L)
                .login("testUser_1")
                .password("testUser_1")
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("testUser_1@gmail.com")
                .birthday(parseDate("1986-01-01"))
                .role(new Role(1L, "Admin")).build();


        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/" + userLogin).get();

        User user = jsonMapper.readValue(output.readEntity(String.class),
                User.class);

        assertEquals("should return status 200", Response.Status.OK.getStatusCode(),
                output.getStatus());
        assertEquals("users should be equal", expectedUser, user);
    }

    @Test(timeout = 15_000L)
    public void t030_getUserByWrongLogin() throws Exception {
        String wrongUserLogin = "wrongLogin";
        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/" + wrongUserLogin).get();

        assertEquals("should return status 404", Response.Status.NOT_FOUND.getStatusCode(),
                output.getStatus());
    }

    @Test(timeout = 15_000L)
    public void t040_createUser() throws Exception {
        int expectedNewId = 6;
        User newUser = User.builder()
                .id(0L)
                .login("testUser_6")
                .password("testUser_6")
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .email("testUser_6@gmail.com")
                .birthday(parseDate("1986-06-06"))
                .role(new Role(1L, "Admin")).build();

        Response output = client.path(USERS_ENDPOINT_ADDRESS)
                .post(newUser);

        assertEquals("should return status 201", Response.Status.CREATED.getStatusCode(),
                output.getStatus());

        newUser.setId(expectedNewId);
        User createdUser = jsonMapper.readValue(output.readEntity(String.class), User.class);
        assertEquals("sent user and user from response should be equal", newUser, createdUser);
    }

    @Test(timeout = 15000L)
    public void t050_createUserWrongParam() throws Exception {
        User newUser = User.builder()
                .id(0L)
                .login("testUser_1")
                .password("p")
                .firstName("f")
                .lastName("l")
                .email("e")
                .birthday(parseDate("2050-01-01"))
                .role(new Role(1L, "Admin")).build();

        int expectedFieldsWithError = 6;

        Response output = client.post(newUser);

        MultivaluedMap<String, String> errors =
                output.readEntity(new GenericType<MultivaluedHashMap<String, String>>() {
                });

        assertEquals("should return status 400", Response.Status.BAD_REQUEST.getStatusCode(),
                output.getStatus());

        assertEquals("six fields with error must be returned",
                errors.size(), expectedFieldsWithError);

        assertThat("listed fields should contain error", errors,
                allOf(
                        hasKey("login"),
                        hasKey("password"),
                        hasKey("firstName"),
                        hasKey("lastName"),
                        hasKey("email"),
                        hasKey("birthday")
                ));
    }

    @Test(timeout = 15_000L)
    public void t060_updateUser() throws Exception {
        User updateUser = User.builder()
                .id(6L)
                .login("testUser_6")
                .password("testUser_6")
                .firstName("firstNameUp")
                .lastName("lastNameUp")
                .email("testUser_6@gmail.com")
                .birthday(parseDate("1986-06-06"))
                .role(new Role(1L, "Admin")).build();

        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/testUser_6")
                .put(updateUser);

        assertEquals("should return status 200", Response.Status.OK.getStatusCode(),
                output.getStatus());

        User userInResponce = jsonMapper.readValue(output.readEntity(String.class), User.class);
        assertEquals(updateUser, userInResponce);
    }

    @Test(timeout = 15_000L)
    public void t070_updateNonexistentUser() throws Exception {
        User updateUser = User.builder()
                .id(6L)
                .login("not_real")
                .password("testUser_6")
                .firstName("firstNameUp")
                .lastName("lastNameUp")
                .email("testUser_6@gmail.com")
                .birthday(parseDate("1986-06-06"))
                .role(new Role(1L, "Admin")).build();

        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/" + updateUser.getLogin())
                .put(updateUser);

        assertEquals("should return status 404", Response.Status.NOT_FOUND.getStatusCode(),
                output.getStatus());
    }

    @Test(timeout = 15_000L)
    public void t080_updateUserInvalidNameBirthday() throws Exception {
        User updateUser = User.builder()
                .id(6L)
                .login("testUser_6")
                .password("testUser_6")
                .firstName("_")
                .lastName("lastNameUp")
                .email("testUser_6@gmail.com")
                .birthday(parseDate("2050-06-06"))
                .role(new Role(1L, "Admin")).build();
        int expectedFieldsWithError = 2;

        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/" + updateUser.getLogin())
                .put(updateUser);

        MultivaluedMap<String, String> errors =
                output.readEntity(new GenericType<MultivaluedHashMap<String, String>>() {
                });

        assertEquals("should return status 400", Response.Status.BAD_REQUEST.getStatusCode(),
                output.getStatus());

        assertEquals("two fields with error must be returned",
                errors.size(), expectedFieldsWithError);

        assertThat(errors,
                allOf(
                        hasKey("firstName"),
                        hasKey("birthday")
                ));
    }

    @Test(timeout = 15_000L)
    public void t090_updateUserChangeLogin() throws Exception {
        User updateUser = User.builder()
                .id(6L)
                .login("testUP_6")
                .password("testUser_6")
                .firstName("lastName")
                .lastName("lastName")
                .email("testUser_6@gmail.com")
                .birthday(parseDate("1986-06-06"))
                .role(new Role(1L, "Admin")).build();
        updateUser.setLogin("LoginUpdate");

        int expectedFieldsWithError = 1;

        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/testUser_6")
                .put(updateUser);

        MultivaluedMap<String, String> errors =
                output.readEntity(new GenericType<MultivaluedHashMap<String, String>>() {
                });

        assertEquals("should return status 400", Response.Status.BAD_REQUEST.getStatusCode(),
                output.getStatus());

        assertEquals("one field with error must be returned",
                expectedFieldsWithError, errors.size());

        assertThat(errors,
                allOf(
                        hasKey("login")
                ));
    }

    @Test(timeout = 15_000L)
    public void t100_deleteUser() throws Exception {
        client.path(USERS_ENDPOINT_ADDRESS);

        List<User> listBefore = jsonMapper.readValue(
                client.path(USERS_ENDPOINT_ADDRESS).get()
                        .readEntity(String.class),
                TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));

        Response output = client
                .path(USERS_ENDPOINT_ADDRESS + "/testUser_6")
                .delete();

        assertEquals("should return status 200", Response.Status.OK.getStatusCode(),
                output.getStatus());

        List<User> listAfter = jsonMapper.readValue(
                client.path(USERS_ENDPOINT_ADDRESS).get().readEntity(String.class),
                TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));

        assertEquals("list after should be less by one",
                listBefore.size(), listAfter.size() + 1);
    }

    @Test(timeout = 15_000L)
    public void t110_deleteNonexistentUser() throws Exception {
        Response output = client.path(USERS_ENDPOINT_ADDRESS + "/unrealLogin")
                .delete();

        assertEquals("should return status 404", Response.Status.NOT_FOUND.getStatusCode(),
                output.getStatus());
    }

    private Date parseDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(str);
    }
}
