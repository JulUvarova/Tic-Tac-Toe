package com.trumpecy.tictactoe.domain.service.user;

import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private User testUser;
    private String testLogin;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testLogin = "integrationtestuser";
        testPassword = "password123";

        testUser = new User();
        testUser.setLogin(testLogin);
        testUser.setPassword(testPassword);
    }

    @Test
    void create_ShouldCreateNewUser() {
        userService.create(testLogin, testPassword);

        User createdUser = userService.getByLogin(testLogin);
        assertNotNull(createdUser);
        assertEquals(testLogin, createdUser.getLogin());
        assertEquals(testPassword, createdUser.getPassword());
        assertNotNull(createdUser.getId());
    }

    @Test
    void create_WithDuplicateLogin_ShouldThrowException() {
        userService.create(testLogin, testPassword);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.create(testLogin, "differentpassword")
        );

        assertEquals("User integrationtestuser already exists", exception.getMessage());
    }

    @Test
    void getByLogin_WhenUserExists_ShouldReturnUser() {
        userService.create(testLogin, testPassword);

        User result = userService.getByLogin(testLogin);

        assertNotNull(result);
        assertEquals(testLogin, result.getLogin());
        assertEquals(testPassword, result.getPassword());
    }

    @Test
    void getByLogin_WhenUserNotExists_ShouldThrowException() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getByLogin("nonexistentuser")
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getById_WhenUserExists_ShouldReturnUser() {
        userService.create(testLogin, testPassword);
        User createdUser = userService.getByLogin(testLogin);

        User result = userService.getById(createdUser.getId());

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals(testLogin, result.getLogin());
    }

    @Test
    void getById_WhenUserNotExists_ShouldThrowException() {
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getById(UUID.randomUUID())
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllUsersPageable_ShouldReturnPaginatedResults() {
        userService.create("user1", "pass1");
        userService.create("user2", "pass2");
        userService.create("user3", "pass3");

        Page<User> result = userService.getAllUsersPageable(null, 0, 2);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 3);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getTotalPages() >= 2);
    }

    @Test
    void getAllUsersPageable_WithSearch_ShouldReturnFilteredResults() {
        userService.create("searchuser1", "pass1");
        userService.create("searchuser2", "pass2");
        userService.create("otheruser", "pass3");

        Page<User> result = userService.getAllUsersPageable("search", 0, 10);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
        assertTrue(result.getContent().stream()
                .allMatch(user -> user.getLogin().toLowerCase().contains("search")));
    }

    @Test
    void getAllUsersPageable_WithEmptySearch_ShouldReturnAllUsers() {
        userService.create("user1", "pass1");
        userService.create("user2", "pass2");

        Page<User> result = userService.getAllUsersPageable("", 0, 10);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
    }

    @Test
    void getAllUsersPageable_WithNullSearch_ShouldReturnAllUsers() {
        userService.create("user1", "pass1");
        userService.create("user2", "pass2");

        Page<User> result = userService.getAllUsersPageable(null, 0, 10);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
    }

    @Test
    void getAllUsersPageable_WithWhitespaceSearch_ShouldReturnAllUsers() {
        userService.create("user1", "pass1");
        userService.create("user2", "pass2");

        Page<User> result = userService.getAllUsersPageable("   ", 0, 10);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
    }

    @Test
    void getAllUsersPageable_WithCaseInsensitiveSearch_ShouldFindUsers() {
        userService.create("TestUser", "pass1");
        userService.create("testuser2", "pass2");

        Page<User> result = userService.getAllUsersPageable("test", 0, 10);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
        assertTrue(result.getContent().stream()
                .allMatch(user -> user.getLogin().toLowerCase().contains("test")));
    }

    @Test
    void getAllUsersPageable_WithPagination_ShouldReturnCorrectPage() {
        userService.create("user1", "pass1");
        userService.create("user2", "pass2");
        userService.create("user3", "pass3");
        userService.create("user4", "pass4");
        userService.create("user5", "pass5");

        Page<User> firstPage = userService.getAllUsersPageable(null, 0, 2);
        Page<User> secondPage = userService.getAllUsersPageable(null, 1, 2);

        assertEquals(0, firstPage.getNumber());
        assertEquals(1, secondPage.getNumber());
        assertEquals(2, firstPage.getContent().size());
        assertNotEquals(firstPage.getContent(), secondPage.getContent());
    }
} 