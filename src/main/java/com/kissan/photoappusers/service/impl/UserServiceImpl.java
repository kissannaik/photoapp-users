package com.kissan.photoappusers.service.impl;

import com.kissan.photoappusers.dao.UserDAO;
import com.kissan.photoappusers.entity.UserEntity;
import com.kissan.photoappusers.repository.UsersRepository;
import com.kissan.photoappusers.service.UserService;
import com.kissan.photoappusers.service.client.AlbumsClient;
import com.kissan.photoappusers.shared.AlbumDTO;
import com.kissan.photoappusers.shared.UserDTO;
import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment environment;
    private RestTemplate restTemplate;
    private AlbumsClient albumsClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           //RestTemplate restTemplate,
                           AlbumsClient albumsClient,
                           Environment environment)
    {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //this.restTemplate = restTemplate;
        this.albumsClient = albumsClient;
        this.environment = environment;
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

    @Override
    public UserDTO getUserAlbums(String userId) {

        UserDTO userDTO = getUser(userId);

        if(userDTO == null)
            throw new UsernameNotFoundException(userId);

        // Microservices innovation using RestTemplate
        /*
        String albumsUrl = String.format(environment.getProperty("albums.url.path"), userId);
        ResponseEntity<List<AlbumDTO>> albumsListResponse = restTemplate.exchange(albumsUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumDTO>>() {});
        List<AlbumDTO> albumsList = albumsListResponse.getBody();
        */

        // Microservices innovation using FeignClient
        List<AlbumDTO> albumsList = null;
        try {
            albumsList = albumsClient.getAlbums(userId);
        }catch (FeignException e){
            logger.debug(e.getLocalizedMessage());
        }

        userDTO.setAlbums(albumsList);

        return userDTO;
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
