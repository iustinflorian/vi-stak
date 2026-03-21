package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserLoginDTO;
import com.gifprojects.vistak.model.GenderType;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceMockTest {
    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userServiceMock;

    @Test
    public void userRegisterTest(){
        // prepare mock
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("username");
        dto.setPassword("password");
        dto.setEmail("email@test.com");
        dto.setGenderType(GenderType.male);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        User mockUser = User.builder()
                .id(1L)
                .username("username")
                .password("encoded_password")
                .email("email@test.com")
                .build();

        // mock behaviour
        when(userRepositoryMock.save(any(User.class))).thenReturn(mockUser);

        // call mock service
        User result = userServiceMock.createUser(dto);

        // assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encoded_password", result.getPassword());

        verify(passwordEncoder).encode("password");
    }

    @Test
    public void userLoginTest(){
        // prepare mock
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("username");
        dto.setPassword("password");

        User mockUser = User.builder()
                .id(1L)
                .username("username")
                .password("encoded_password")
                .email("email@test.com")
                .build();

        // mock behaviour
        when(userRepositoryMock.findByUsername("username")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        // call mock service
        User result = userServiceMock.loginUser(dto);

        // assertions
        assertNotNull(result);
        assertEquals("username", result.getUsername());

        verify(passwordEncoder).matches("password", "encoded_password");
    }

    @Test
    public void fetchUser(){
        User mockUser = User.builder()
                .id(1L)
                .username("username")
                .password("encoded_password")
                .email("email@test.com")
                .build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userServiceMock.fetchUser(1L);

        assertNotNull(result);
        assertEquals(result.getUsername(), mockUser.getUsername());
        assertEquals(result.getId(), mockUser.getId());
    }

    @Test
    void fetchUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepositoryMock.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceMock.fetchUser(99L));
    }
}
