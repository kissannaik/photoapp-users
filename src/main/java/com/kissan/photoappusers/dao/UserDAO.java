package com.kissan.photoappusers.dao;

import com.kissan.photoappusers.ui.model.request.UserRequest;
import com.kissan.photoappusers.ui.model.response.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDAO {
    private final Map<String, UserResponse> users = new ConcurrentHashMap<>();

    public UserResponse[] getAllUsers(){
        return users.values().toArray(new UserResponse[0]);
    }

    public UserResponse getUser(String userId){
        if(userId !=null) {
            return users.get(userId);
        }
        return null;
    }

    public UserResponse addUser(UserRequest userRequest){
        if(validateRequest(userRequest)) {
            UserResponse userResponse = new UserResponse();
            userResponse.setFirstName(userRequest.getFirstName());
            userResponse.setLastName(userRequest.getLastName());
            userResponse.setEmail(userRequest.getEmail());
            userResponse.setUserId(createUserID());

            users.put(userResponse.getUserId(), userResponse);
            return userResponse;
        }
        return null;
    }

    public UserResponse updateUser(String userId, UserRequest userRequest){
        if(validateRequest(userRequest)
                && userId != null
                && users.containsKey(userId)) {
            UserResponse userResponse = users.get(userId);
            userResponse.setFirstName(userRequest.getFirstName());
            userResponse.setLastName(userRequest.getLastName());

            users.put(userResponse.getUserId(), userResponse);
            return userResponse;
        }
        return null;
    }

    public boolean deleteUser(String userId){
        if(userId !=null) {
            users.remove(userId);
            return true;
        }
        return false;
    }

    private String createUserID(){
        return UUID.randomUUID().toString();
    }

    private boolean validateRequest(UserRequest userRequest){
        if(userRequest != null && userRequest.getEmail() != null && userRequest.getPassword() != null)
            return true;

        return false;
    }
}
