package com.gifprojects.vistak.mapping;

import com.gifprojects.vistak.mapping.userDTO.UserFetchDTO;
import com.gifprojects.vistak.model.User;

public class MapResponse {
    public static UserFetchDTO mapUserResponse(User user){
        UserFetchDTO response = new UserFetchDTO();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}
