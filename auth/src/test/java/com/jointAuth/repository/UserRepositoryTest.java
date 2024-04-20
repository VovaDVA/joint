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

    @Test
    public void testFindByIdUserExists() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userRepository
                .findById(id))
                .thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByIdUserDoesNotExist() {
        Long id = 99L;
        when(userRepository
                .findById(id))
                .thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(id);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByIdNullId() {
        Long id = null;
        when(userRepository
                .findById(id))
                .thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(id);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByIdNegativeId() {
        Long id = -1L;
        when(userRepository
                .findById(id))
                .thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(id);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testSaveNewUser() {
        User user = new User();
        user.setEmail("newuser@gmail.com");
        when(userRepository
                .save(user))
                .thenReturn(user);

        User result = userRepository.save(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveExistingUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existinguser@gmail.com");
        when(userRepository
                .save(existingUser))
                .thenReturn(existingUser);

        User result = userRepository.save(existingUser);

        assertEquals(existingUser, result);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testSaveNullUser() {
        User user = null;
        when(userRepository
                .save(user))
                .thenReturn(user);

        User result = userRepository.save(user);

        assertNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveUserWithMissingFields() {
        User user = new User();
        user.setEmail(null);
        when(userRepository
                .save(user))
                .thenReturn(user);

        User result = userRepository.save(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveUserWithInvalidFields() {
        User user = new User();
        user.setEmail("invalid_email");
        when(userRepository
                .save(user))
                .thenReturn(user);

        User result = userRepository.save(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }
}
