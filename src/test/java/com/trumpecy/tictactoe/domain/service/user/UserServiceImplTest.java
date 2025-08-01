package com.trumpecy.tictactoe.domain.service.user;

import com.trumpecy.tictactoe.datasource.repository.user.UserRepository;
import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UUID testUserId;
    private String testLogin;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testLogin = "testuser";
        testPassword = "password123";

        testUser = new User();
        testUser.setId(testUserId);
        testUser.setLogin(testLogin);
        testUser.setPassword(testPassword);
    }

    @Test
    void getAllUsersPageable_WithSearch_ShouldReturnFilteredUsers() {
        String searchTerm = "test";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userRepository.findAllLoginContains(searchTerm, page, size)).thenReturn(userPage);

        Page<User> result = userService.getAllUsersPageable(searchTerm, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAllLoginContains(searchTerm, page, size);
        verify(userRepository, never()).findAll(anyInt(), anyInt());
    }

    @Test
    void getAllUsersPageable_WithEmptySearch_ShouldReturnAllUsers() {
        String searchTerm = "";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userRepository.findAll(page, size)).thenReturn(userPage);

        Page<User> result = userService.getAllUsersPageable(searchTerm, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAll(page, size);
        verify(userRepository, never()).findAllLoginContains(anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllUsersPageable_WithNullSearch_ShouldReturnAllUsers() {
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userRepository.findAll(page, size)).thenReturn(userPage);

        Page<User> result = userService.getAllUsersPageable(null, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAll(page, size);
        verify(userRepository, never()).findAllLoginContains(anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllUsersPageable_WithWhitespaceSearch_ShouldReturnAllUsers() {
        String searchTerm = "   ";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userRepository.findAll(page, size)).thenReturn(userPage);

        Page<User> result = userService.getAllUsersPageable(searchTerm, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAll(page, size);
        verify(userRepository, never()).findAllLoginContains(anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllUsersPageable_WithTrimmedSearch_ShouldUseTrimmedValue() {
        String searchTerm = "  test  ";
        String expectedTrimmedSearch = "test";
        int page = 0;
        int size = 5;
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        when(userRepository.findAllLoginContains(expectedTrimmedSearch, page, size)).thenReturn(userPage);

        Page<User> result = userService.getAllUsersPageable(searchTerm, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAllLoginContains(expectedTrimmedSearch, page, size);
    }

    @Test
    void getById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        User result = userService.getById(testUserId);

        assertNotNull(result);
        assertEquals(testUser, result);
        assertEquals(testUserId, result.getId());
        assertEquals(testLogin, result.getLogin());
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getById_WhenUserNotExists_ShouldThrowEntityNotFoundException() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getById(testUserId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getByLogin_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.of(testUser));

        User result = userService.getByLogin(testLogin);

        assertNotNull(result);
        assertEquals(testUser, result);
        assertEquals(testUserId, result.getId());
        assertEquals(testLogin, result.getLogin());
        verify(userRepository).findByLogin(testLogin);
    }

    @Test
    void getByLogin_WhenUserNotExists_ShouldThrowEntityNotFoundException() {
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getByLogin(testLogin)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByLogin(testLogin);
    }

    @Test
    void create_WhenUserNotExists_ShouldCreateUser() {
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.empty());

        userService.create(testLogin, testPassword);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_WhenUserAlreadyExists_ShouldThrowUserAlreadyExistsException() {
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.of(testUser));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.create(testLogin, testPassword)
        );

        assertEquals("User testuser already exists", exception.getMessage());
        verify(userRepository).findByLogin(testLogin);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void create_WithEmptyLogin_ShouldThrowUserAlreadyExistsException() {
        String emptyLogin = "";
        when(userRepository.findByLogin(emptyLogin)).thenReturn(Optional.of(testUser));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.create(emptyLogin, testPassword)
        );

        assertEquals("User  already exists", exception.getMessage());
    }

    @Test
    void create_WithNullLogin_ShouldThrowUserAlreadyExistsException() {
        when(userRepository.findByLogin(null)).thenReturn(Optional.of(testUser));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.create(null, testPassword)
        );

        assertEquals("User null already exists", exception.getMessage());
    }
}