package com.jointAuth.converter;

import com.jointAuth.model.user.User;
import com.jointAuth.model.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserConverterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testConvertToDtoConvertsUserToUserDTO() {
        String password = "PassViktor123=";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Viktor");
        user.setLastName("Doramov");
        user.setEmail("vidor@gmail.com");
        user.setPassword(encodedPassword);
        user.setId(1L);
        user.setRegistrationDate(new Date());
        user.setLastLogin(null);

        UserDTO dto = UserConverter.convertToDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRegistrationDate(), dto.getRegistrationDate());
        assertEquals(user.getLastLogin(), dto.getLastLogin());
    }

    @Test
    public void testConvertToDtoNullFieldsInUserReturnsDtoWithNullFields() {
        String password = "passForTest1+";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Alexander");
        user.setLastName(null);
        user.setEmail("Sanya24@example.com");
        user.setPassword(encodedPassword);
        user.setId(1L);
        user.setRegistrationDate(new Date());
        user.setLastLogin(null);

        UserDTO dto = UserConverter.convertToDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertNull(dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRegistrationDate(), dto.getRegistrationDate());
        assertNull(dto.getLastLogin());
    }

    @Test
    public void testConvertToDtoEmptyUserReturnsEmptyDto() {
        User emptyUser = new User();

        UserDTO emptyDto = UserConverter.convertToDto(emptyUser);

        assertEquals(0L, emptyDto.getId());
        assertNull(emptyDto.getLastName());
        assertNull(emptyDto.getEmail());
        assertNull(emptyDto.getRegistrationDate());
        assertNull(emptyDto.getLastLogin());
    }

    @Test
    public void testConvertToDtoMinimalUserData() {
        User minimalUser = new User();
        minimalUser.setId(1L);
        minimalUser.setFirstName("Vladimir");
        minimalUser.setEmail("john@gmail.com");

        UserDTO dto = UserConverter.convertToDto(minimalUser);

        assertEquals(minimalUser.getId(), dto.getId());
        assertEquals(minimalUser.getFirstName(), dto.getFirstName());
        assertEquals(minimalUser.getEmail(), dto.getEmail());

        assertNull(dto.getLastName());
        assertNull(dto.getRegistrationDate());
        assertNull(dto.getLastLogin());
    }

    @Test
    public void testConvertToDtoNullUser() {
        User nullUser = null;

        UserDTO dto = UserConverter.convertToDto(nullUser);

        assertNull(dto);
    }
}