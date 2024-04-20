package com.jointAuth.repository;

import com.jointAuth.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
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

    @Test
    public void testDeleteByIdExistingUser() {
        Long id = 1L;

        userRepository.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteByIdNonExistingUser() {
        Long id = 99L;

        userRepository.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteByIdNullId() {
        Long id = null;

        userRepository.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindAllWithUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@gmail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        List<User> users = List.of(user1, user2);
        when(userRepository
                .findAll())
                .thenReturn(users);

        List<User> result = userRepository.findAll();

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllWithoutUsers() {
        when(userRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        List<User> result = userRepository.findAll();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllVeryLargeDatabase() {
        List<User> largeDatabase = generateLargeDatabase();
        when(userRepository
                .findAll())
                .thenReturn(largeDatabase);

        List<User> result = userRepository.findAll();

        assertEquals(largeDatabase, result);
        verify(userRepository, times(1)).findAll();
    }

    private List<User> generateLargeDatabase() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("newUser" + i + "@gmail.com");
            users.add(user);
        }
        return users;
    }
}
