package com.kissan.photoappusers.ui.controller;

import com.kissan.photoappusers.service.UserService;
import com.kissan.photoappusers.shared.UserDTO;
import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;

    @GetMapping
    public ResponseEntity<UserResponse[]> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "50") int limit,
                           @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}",
                produces = {MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId){
        System.out.println("userId in getUser = " + userId+
                env.getProperty("user.server.port") +
                "; with token = " +env.getProperty("token.secret"));
        UserDTO userDTO = userService.getUser(userId);
        System.out.println("userResponse in getUser = " + userDTO);
        if(userDTO !=null){
            UserResponse userResponse = (new ModelMapper()).map(userDTO, UserResponse.class);
            System.out.println("userResponse in adduser = " + userResponse);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE}
            )
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest userRequest){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = modelMapper.map(userRequest, UserDTO.class);

        userDTO = userService.createUser(userDTO);

        UserResponse userResponse = modelMapper.map(userDTO, UserResponse.class);
        System.out.println("userResponse in adduser = " + userResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping(value = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserRequest userRequest){

        UserResponse storedUser = userService.updateUser(userId, userRequest);

        if(storedUser != null)
            return ResponseEntity.ok().body(storedUser);

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId){
        if(userService.deleteUser(userId))
            return ResponseEntity.ok().build();

        return ResponseEntity.noContent().build();
    }
}
