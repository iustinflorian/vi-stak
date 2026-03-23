package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserLoginDTO;
import com.gifprojects.vistak.mapping.userDTO.UserUpdateDTO;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserCreateDTO data){
        if (userRepository.findByUsername(data.getUsername()).isPresent()){
            throw new RuntimeException("username already used");
        }
        if (userRepository.findByEmail(data.getEmail()).isPresent()){
            throw new RuntimeException("email already used");
        }

        if (userRepository.getUserByEmail(data.getEmail()) != null){
        }

        User newUser = User.builder()
                        .username(data.getUsername())
                        .password(passwordEncoder.encode(data.getPassword()))
                        .email(data.getEmail())
                        .genderType(data.getGenderType())
                        .taskList(new ArrayList<>())
                        .build();

        return userRepository.save(newUser);
    }

    public User loginUser(UserLoginDTO data){
        User currUser = userRepository.findByUsername(data.getUsername())
                .orElseThrow(() -> new RuntimeException("not found"));

        if (!passwordEncoder.matches(data.getPassword(), currUser.getPassword())){
            throw new RuntimeException("unauthorized");
        }

        return currUser;
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
            currUser.setPassword(passwordEncoder.encode(data.getPassword()));
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
