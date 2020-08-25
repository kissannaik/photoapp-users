package com.kissan.photoappusers.service;

import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;

public interface UserService {

    UserResponse[] getAllUsers();

    UserResponse getUser(String userId);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(String userId, UserRequest userRequest);

    boolean deleteUser(String userId);
}
