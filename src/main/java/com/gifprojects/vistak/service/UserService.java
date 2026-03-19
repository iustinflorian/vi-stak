package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserUpdateDTO;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateDTO data){
        User newUser = User.builder()
                        .username(data.getUsername())
                        .password(data.getPassword())
                        .email(data.getEmail())
                        .genderType(data.getGenderType())
                        .build();

        return userRepository.save(newUser);
    }

    public User fetchUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public User updateUser(UserUpdateDTO data, Long userId){
        User currUser = fetchUser(userId);

        if(data.getUsername() != null && !data.getUsername().isBlank()){
            if (userRepository.getUserByUsername(data.getUsername()) != null){
                throw new RuntimeException("username already used");
            }
            currUser.setUsername(data.getUsername());
        }

        if(data.getEmail() != null && !data.getEmail().isBlank()){
            if (userRepository.getUserByEmail(data.getEmail()) != null){
                throw new RuntimeException("email already used");
            }
            currUser.setEmail(data.getEmail());
        }

        if(data.getPassword() != null && !data.getPassword().isBlank()){
            currUser.setPassword(data.getPassword());
        }

        return userRepository.save(currUser);
    }

    public void deleteUser(Long userId){
        if (!userRepository.existsById(userId)){
            throw new RuntimeException("not found");
        }
        userRepository.deleteById(userId);
    }
}
