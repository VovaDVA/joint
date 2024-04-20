package com.jointAuth.converter;

import com.jointAuth.model.User;
import com.jointAuth.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserConverterTest {
    @Test
    public void testConvertToDtoConvertsUserToUserDTO() {
        User user = new User();

        user.setFirstName("Viktor");
        user.setLastName("Doramov");
        user.setEmail("vidor@gmail.com");
        user.setPassword("PassViktor123=");
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
        User user = new User();

        user.setFirstName("Alexander");
        user.setLastName(null);
        user.setEmail("Sanya24@example.com");
        user.setPassword("passForTest1+");
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
}