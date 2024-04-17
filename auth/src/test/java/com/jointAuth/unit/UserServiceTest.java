package com.jointAuth.unit;

import com.jointAuth.model.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUserExistingEmailThrowsException() {
        User existingUser = new User("Dan", "Dorin", "testing@gmail.com", "password");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        User newUser = new User("Vladimir", "Proven", "testing@gmail.com", "newpassword");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserInvalidPasswordThrowsException() {
        User newUser = new User("Vladimir", "Proven", "vlprovin@gmail.com", "password");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserValidUserReturnsRegisteredUser() {
        User newUser = new User("Vladimir", "Proven", "vlprovin@gmail.com", "PasswordTest123@");

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        when(userRepository.save(any())).thenReturn(newUser);

        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");

        User registeredUser = userService.register(newUser);

        assertNotNull(registeredUser);

        assertEquals("Vladimir", registeredUser.getFirstName());
        assertEquals("Proven", registeredUser.getLastName());
        assertEquals("vlprovin@gmail.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());

        assertNotNull(registeredUser.getRegistrationDate());
    }


}
