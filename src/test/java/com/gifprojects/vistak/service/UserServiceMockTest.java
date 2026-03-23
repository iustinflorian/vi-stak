package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserLoginDTO;
import com.gifprojects.vistak.mapping.userDTO.UserUpdateDTO;
import com.gifprojects.vistak.model.GenderType;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    public void userRegisterTest_ShouldThrowException_WhenUsernameAlreadyUsed() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("existing_username");
        dto.setPassword("password");
        dto.setEmail("email@test.com");
        dto.setGenderType(GenderType.male);

        User existingUser = User.builder()
                .id(1L)
                .username("existing_username")
                .password("encoded_password")
                .email("email1@test.com")
                .build();

        when(userRepositoryMock.findByUsername("existing username")).thenReturn(Optional.of(existingUser));

        assertThrows(RuntimeException.class, () -> userServiceMock.createUser(dto));
        verify(userRepositoryMock, never()).save(any(User.class));
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
    void userLoginTest_ShouldThrowException_WhenUserNotFound() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("username");
        dto.setPassword("password");

        when(userRepositoryMock.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceMock.loginUser(dto));
    }

    @Test
    void userLoginTest_ShouldThrowException_WhenPasswordMismatch() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("username");
        dto.setPassword("wrong_password");

        User mockUser = User.builder()
                .id(1L)
                .username("username")
                .password("encoded_password")
                .build();

        when(userRepositoryMock.findByUsername("username")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userServiceMock.loginUser(dto));
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

    @Test
    public void updateUserTest_Success() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("new_username");
        dto.setEmail("new_email@test.com");
        dto.setPassword("new_password");

        User mockUser = User.builder()
                .id(1L)
                .username("old_username")
                .email("old_email@test.com")
                .password("old_password")
                .build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepositoryMock.getUserByUsername("new_username")).thenReturn(null);
        when(userRepositoryMock.getUserByEmail("new_email@test.com")).thenReturn(null);
        when(passwordEncoder.encode("new_password")).thenReturn("new_encoded_password");
        
        when(userRepositoryMock.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userServiceMock.updateUser(dto, 1L);

        assertNotNull(result);
        assertEquals("new_username", result.getUsername());
        assertEquals("new_email@test.com", result.getEmail());
        assertEquals("new_encoded_password", result.getPassword());

        verify(userRepositoryMock).save(mockUser);
    }

    @Test
    public void updateUserTest_ShouldThrowException_WhenUsernameAlreadyUsed() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("existing_username");

        User mockUser = User.builder().id(1L).build();
        User existingUser = User.builder().id(2L).username("existing_username").build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepositoryMock.getUserByUsername("existing_username")).thenReturn(existingUser);

        assertThrows(RuntimeException.class, () -> userServiceMock.updateUser(dto, 1L));
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    public void updateUserTest_ShouldThrowException_WhenEmailAlreadyUsed() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("existing@test.com");

        User mockUser = User.builder().id(1L).build();
        User existingUser = User.builder().id(2L).email("existing@test.com").build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepositoryMock.getUserByEmail("existing@test.com")).thenReturn(existingUser);

        assertThrows(RuntimeException.class, () -> userServiceMock.updateUser(dto, 1L));
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    public void deleteUserTest_Success() {
        when(userRepositoryMock.existsById(1L)).thenReturn(true);
        doNothing().when(userRepositoryMock).deleteById(1L);

        assertDoesNotThrow(() -> userServiceMock.deleteUser(1L));

        verify(userRepositoryMock).deleteById(1L);
    }

    @Test
    public void deleteUserTest_ShouldThrowException_WhenUserNotFound() {
        when(userRepositoryMock.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userServiceMock.deleteUser(99L));
        verify(userRepositoryMock, never()).deleteById(any());
    }
}
