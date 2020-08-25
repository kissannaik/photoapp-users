package com.kissan.photoappusers.service.impl;

import com.kissan.photoappusers.dao.UserDAO;
import com.kissan.photoappusers.service.UserService;
import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserResponse[] getAllUsers() {

        return userDAO.getAllUsers();
    }

    @Override
    public UserResponse getUser(String userId) {

        return userDAO.getUser(userId);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        return userDAO.addUser(userRequest);
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        return userDAO.updateUser(userId, userRequest);
    }

    @Override
    public boolean deleteUser(String userId) {
        return userDAO.deleteUser(userId);
    }


}
