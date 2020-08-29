package com.kissan.photoappusers.service;

import com.kissan.photoappusers.shared.UserDTO;
import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse[] getAllUsers();

    UserDTO getUser(String userId);

    UserDTO getUserAlbums(String userId);

    UserDTO getUserByEmail(String email);

    UserDTO createUser(UserDTO userDTO);

    UserResponse updateUser(String userId, UserRequest userRequest);

    boolean deleteUser(String userId);
}
