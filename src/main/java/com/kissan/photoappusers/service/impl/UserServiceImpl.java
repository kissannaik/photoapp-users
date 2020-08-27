package com.kissan.photoappusers.service.impl;

import com.kissan.photoappusers.dao.UserDAO;
import com.kissan.photoappusers.entity.UserEntity;
import com.kissan.photoappusers.repository.UsersRepository;
import com.kissan.photoappusers.service.UserService;
import com.kissan.photoappusers.shared.UserDTO;
import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponse[] getAllUsers() {

        return userDAO.getAllUsers();
    }

    @Override
    public UserDTO getUser(String userId) {

        UserEntity userEntity = usersRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException(userId);

        return (new ModelMapper()).map(userEntity, UserDTO.class);
    }

    /*
      Used to Login
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        return (new ModelMapper()).map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setUserId(UUID.randomUUID().toString());
        //Encrypt Password
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        // Save Db
        usersRepository.save(userEntity);
        userDTO = modelMapper.map(userEntity, UserDTO.class);

        return userDTO;
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        return userDAO.updateUser(userId, userRequest);
    }

    @Override
    public boolean deleteUser(String userId) {
        return userDAO.deleteUser(userId);
    }

    /*
     Called by Spring Security for Authentication
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserEntity userEntity = usersRepository.findByEmail(userName);

        if(userEntity == null)
            throw new UsernameNotFoundException(userName);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }
}
