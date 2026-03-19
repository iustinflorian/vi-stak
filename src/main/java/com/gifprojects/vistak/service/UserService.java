package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.UserCreateDTO;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void createUser(@NonNull UserCreateDTO data){
        User newUser = User.builder()
                        .username(data.getUsername())
                        .password(data.getPassword())
                        .email(data.getEmail())
                        .genderType(data.getGenderType())
                        .build();

        userRepository.save(newUser);
    }
}
