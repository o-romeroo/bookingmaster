package com.gramos22.bookingmaster;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import com.gramos22.bookingmaster.entities.User;
import com.gramos22.bookingmaster.repositories.UserRepository;
import com.gramos22.bookingmaster.services.UserService;
import com.gramos22.bookingmaster.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        UserRequest request = new UserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setDocument("123456789");

        userService.addUser(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setFirst_name("John");
        user.setLast_name("Doe");
        user.setEmail("john.doe@example.com");
        user.setDocument("123456789");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1);

        assertNotNull(response);
        assertEquals("John", response.getFirst_name());
        assertEquals("Doe", response.getLast_name());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void testDeleteUserById() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUserById(1);

        verify(userRepository, times(1)).delete(user);
    }
}