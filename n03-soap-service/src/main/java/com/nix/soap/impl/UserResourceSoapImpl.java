package com.nix.soap.impl;

import com.nix.dao.UserDao;
import com.nix.model.User;
import com.nix.soap.UserResourceSoap;
import com.nix.soap.exception.UserValidationException;
import com.nix.soap.exception.ValidationExceptionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebService(endpointInterface = "com.nix.soap.UserResourceSoap")
public class UserResourceSoapImpl implements UserResourceSoap {

    private static final Logger log = LoggerFactory.getLogger(RoleResourceSoapImpl.class);

    private UserDao userDao;
    private Validator validator;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("invoke getAllUsers");
        return userDao.findAll();
    }

    @Override
    public User getUserByLogin(String login) {
        log.debug("invoke getUserByLogin with login: {}", login);
        User user = userDao.findByLogin(login);
        if (user != null) {
            return user;
        } else {
            throw new WebServiceException("user with login [" + login + "] not found");
        }
    }

    @Override
    public User createUser(User newUser) throws Exception {
        log.debug("invoke createUser with user: {}", newUser);
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);

        boolean isLoginExist = false;
        if (userDao.findByLogin(newUser.getLogin()) != null) {
            isLoginExist = true;
        }

        if (violations.size() > 0 || isLoginExist) {
            List<ValidationExceptionDetails> listErrors = new ArrayList<>();
            violations.forEach(v -> listErrors.add(
                    new ValidationExceptionDetails(v.getPropertyPath().toString(),
                            v.getMessage())));
            if (isLoginExist) {
                listErrors.add(new ValidationExceptionDetails("login", "non.unique.login"));
            }
            throw new UserValidationException(listErrors);
        }

        try {
            userDao.create(newUser);
        } catch (Exception e) {
            log.error("can not create user: {}; exception: {}", newUser, e);
            throw new WebServiceException("can not save user", e);
        }

        return userDao.findByLogin(newUser.getLogin());
    }

    @Override
    public User updateUser(User updatedUser, String login) {
        return null;
    }

    @Override
    public void deleteUser(String login) {

    }
}
