package com.gifprojects.vistak.controller;

import com.gifprojects.vistak.mapping.MapResponse;
import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserFetchDTO;
import com.gifprojects.vistak.mapping.userDTO.UserLoginDTO;
import com.gifprojects.vistak.mapping.userDTO.UserUpdateDTO;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserFetchDTO> createUser(@RequestBody UserCreateDTO data){
        User newUser = userService.createUser(data);
        UserFetchDTO response = MapResponse.mapUserResponse(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserFetchDTO> loginUser(@RequestBody UserLoginDTO data){
        User newUser = userService.loginUser(data);
        UserFetchDTO response = MapResponse.mapUserResponse(newUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserFetchDTO> fetchUser(@RequestHeader("X-Auth-User-Id") Long userId){
        User newUser = userService.fetchUser(userId);
        UserFetchDTO response = MapResponse.mapUserResponse(newUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update-acc")
    public ResponseEntity<UserFetchDTO> updateUser(
            @RequestBody UserUpdateDTO data,
            @RequestHeader("X-Auth-User-Id") Long userId)
    {
        User newUser = userService.updateUser(data, userId);
        UserFetchDTO response = MapResponse.mapUserResponse(newUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-acc")
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-Auth-User-Id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}