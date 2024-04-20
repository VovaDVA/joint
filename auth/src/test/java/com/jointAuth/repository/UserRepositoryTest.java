package com.jointAuth.repository;

import com.jointAuth.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByEmailUserExists() {
        String email = "test@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setFirstName("Katya");
        user.setLastName("Solovay");
        user.setEmail(email);
        user.setPassword("Pass123!+A");
        when(userRepository
                .findByEmail(email))
                .thenReturn(user);

        User result = userRepository.findByEmail(email);

        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmailUserDoesNotExist() {
        String email = "nonexistent@gmail.com";
        when(userRepository
                .findByEmail(email))
                .thenReturn(null);

        User result = userRepository.findByEmail(email);

        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmailEmptyEmail() {
        String email = "";
        when(userRepository
                .findByEmail(email))
                .thenReturn(null);

        User result = userRepository.findByEmail(email);

        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmailInvalidEmailFormat() {
        String email = "invalid_email";
        when(userRepository
                .findByEmail(email))
                .thenReturn(null);

        User result = userRepository.findByEmail(email);

        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmailSpecialCharactersEmail() {
        String email = "test+test@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setId(1L);
        when(userRepository
                .findByEmail(email))
                .thenReturn(user);

        User result = userRepository.findByEmail(email);

        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    
}
