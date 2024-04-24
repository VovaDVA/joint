package com.jointAuth.converter;

import com.jointAuth.bom.profile.ProfileBom;
import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class ProfileConverterTest {

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("forever05@gmail.com");
        return user;
    }

    @Test
    public void testConverterToBomSuccess() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setDescription("Test description");
        profile.setBirthday("01.01.2004");
        profile.setCountry("Russia");
        profile.setCity("Moscow");
        profile.setPhone("+1234567890");
        profile.setLastEdited(new Date());
        profile.setUser(createTestUser());

        Optional<Profile> optionalProfile = Optional.of(profile);

        ProfileBom bom = ProfileConverter.converterToBom(optionalProfile);

        assertEquals(1L, bom.getProfileId());
        assertEquals(profile.getUser().getId(), bom.getUserId());
        assertEquals("Test description", bom.getDescription());
        assertEquals("01.01.2004", bom.getBirthday());
        assertEquals("Russia", bom.getCountry());
        assertEquals("Moscow", bom.getCity());
        assertEquals("+1234567890", bom.getPhone());
        assertNotNull(bom.getLastEdited());
    }

    @Test
    public void testConverterToBomEmptyOptional() {
        Optional<Profile> emptyOptional = Optional.empty();

        assertThrows(NoSuchElementException.class, () -> {
            ProfileConverter.converterToBom(emptyOptional);
        });
    }

    @Test
    public void testConverterToBomNullFields() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setDescription(null);
        profile.setBirthday(null);
        profile.setCountry(null);
        profile.setCity(null);
        profile.setPhone(null);
        profile.setLastEdited(null);
        profile.setUser(createTestUser());

        Optional<Profile> optionalProfile = Optional.of(profile);

        ProfileBom bom = ProfileConverter.converterToBom(optionalProfile);

        assertNull(bom.getDescription(), "Description should be null");
        assertNull(bom.getBirthday(), "Birthday should be null");
        assertNull(bom.getCountry(), "Country should be null");
        assertNull(bom.getCity(), "City should be null");
        assertNull(bom.getPhone(), "Phone should be null");
    }

    @Test
    public void testConverterToBomNullUser() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setDescription("Test description");
        profile.setBirthday("05.05.2004");
        profile.setCountry("Russia");
        profile.setCity("Moscow");
        profile.setPhone("+1234567890");
        profile.setLastEdited(new Date());
        profile.setUser(null);

        Optional<Profile> optionalProfile = Optional.of(profile);

        assertThrows(NullPointerException.class, () -> {
            ProfileConverter.converterToBom(optionalProfile);
        });
    }

    @Test
    public void testConverterToBomInvalidDataTypes() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setDescription("Test description");
        profile.setBirthday("01.04.2003");
        profile.setCountry("Russia");
        profile.setCity("Moscow");
        profile.setPhone("InvalidPhone");
        profile.setLastEdited(new Date());
        profile.setUser(createTestUser());

        Optional<Profile> optionalProfile = Optional.of(profile);

        ProfileBom bom = ProfileConverter.converterToBom(optionalProfile);
        assertEquals(profile.getPhone(), bom.getPhone());
    }
}
