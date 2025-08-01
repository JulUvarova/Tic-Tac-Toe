package com.trumpecy.tictactoe.web.controller;

import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.domain.service.user.UserService;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.web.model.PageDto;
import com.trumpecy.tictactoe.web.model.user.UserDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UUID testUserId;
    private String testLogin;
    private UserDtoResponse testUserDto;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testLogin = "testuser";

        testUser = new User();
        testUser.setId(testUserId);
        testUser.setLogin(testLogin);
        testUser.setPassword("password123");

        testUserDto = new UserDtoResponse();
        testUserDto.setId(testUserId);
        testUserDto.setLogin(testLogin);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userService.getById(testUserId)).thenReturn(testUser);

        ResponseEntity<UserDtoResponse> response = userController.getUserById(testUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUserId, response.getBody().getId());
        assertEquals(testLogin, response.getBody().getLogin());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(userService).getById(testUserId);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        when(userService.getById(testUserId)).thenThrow(new EntityNotFoundException("User not found"));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userController.getUserById(testUserId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userService).getById(testUserId);
    }

    @Test
    void getAllUsers_WithoutSearch_ShouldReturnAllUsers() {
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userService.getAllUsersPageable(null, page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(testUserId, response.getBody().getContent().get(0).getId());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(userService).getAllUsersPageable(null, page, size);
    }

    @Test
    void getAllUsers_WithSearch_ShouldReturnFilteredUsers() {
        String search = "test";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userService.getAllUsersPageable(search, page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(search, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(testUserId, response.getBody().getContent().get(0).getId());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(userService).getAllUsersPageable(search, page, size);
    }

    @Test
    void getAllUsers_WithEmptySearch_ShouldReturnAllUsers() {
        String search = "";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userService.getAllUsersPageable("", page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(search, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(userService).getAllUsersPageable("", page, size);
    }

    @Test
    void getAllUsers_WithWhitespaceSearch_ShouldReturnAllUsers() {
        String search = "   ";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userService.getAllUsersPageable("   ", page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(search, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(userService).getAllUsersPageable("   ", page, size);
    }

    @Test
    void getAllUsers_WithPagination_ShouldReturnCorrectPage() {
        int page = 1;
        int size = 3;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), 10);

        when(userService.getAllUsersPageable(null, page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(1, response.getBody().getNumber());
        assertEquals(10, response.getBody().getTotalElements());
        verify(userService).getAllUsersPageable(null, page, size);
    }

    @Test
    void getAllUsers_WithDefaultSize_ShouldUseDefaultValue() {
        int page = 0;
        int defaultSize = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, defaultSize), users.size());

        when(userService.getAllUsersPageable(null, page, defaultSize)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(null, page, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(userService).getAllUsersPageable(null, page, defaultSize);
    }

    @Test
    void getAllUsers_WithMultipleUsers_ShouldReturnAllUsers() {
        int page = 0;
        int size = 5;
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setLogin("user1");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setLogin("user2");

        List<User> users = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userService.getAllUsersPageable(null, page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("user1", response.getBody().getContent().get(0).getLogin());
        assertEquals("user2", response.getBody().getContent().get(1).getLogin());
        verify(userService).getAllUsersPageable(null, page, size);
    }

    @Test
    void getAllUsers_WithEmptyPage_ShouldReturnEmptyPage() {
        int page = 0;
        int size = 5;
        Page<User> userPage = new PageImpl<>(Arrays.asList(), PageRequest.of(page, size), 0);

        when(userService.getAllUsersPageable(null, page, size)).thenReturn(userPage);

        ResponseEntity<PageDto<UserDtoResponse>> response = userController.getAllUsers(null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getContent().size());
        assertEquals(0, response.getBody().getTotalElements());
        verify(userService).getAllUsersPageable(null, page, size);
    }
} 