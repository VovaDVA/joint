package com.jointProfile.service;

import com.jointProfile.bom.profile.ProfileBom;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.repository.ProfileRepository;
import com.jointProfile.utils.RemoteFileUploader;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks /* Автоматическое внедрение зависимостей в тестируемые классы.
    Фреймворк создает экземпляр класса, помеченного этой аннотацией, и внедряет в него все зависимости,
    помеченные аннотациями @Mock */
    private ProfileService profileService;
    @Mock
    private RemoteFileUploader fileUploader;
    @Mock
    private ProfileRepository profileRepository;

    private Profiles currentProfile;

    @BeforeEach
    void setUp() {
        currentProfile = new Profiles();
        currentProfile.setId(1L);
        currentProfile.setUserId(1L);
    }

    @Test
    void testUpdateProfileWithValidInputs() {

        currentProfile.setDescription("Старое описание");
        currentProfile.setBirthday("11.10.2001");
        currentProfile.setCountry("США");
        currentProfile.setCity("Нью-Йорк");
        currentProfile.setPhone("555-1234");


        ProfileDTO updatedProfile = new ProfileDTO();

        updatedProfile.setDescription("Новое описание");
        updatedProfile.setBirthday("12.01.2011");
        updatedProfile.setCountry("Италия");
        updatedProfile.setCity("Рим");
        updatedProfile.setPhone("555-5678");

        when(profileRepository
                .findById(1L))
                .thenReturn(Optional.of(currentProfile)); // иммитация возвращения существующего профиля

        when(profileRepository
                .save(currentProfile))
                .thenReturn(currentProfile);

        ProfileBom result = profileService.updateProfile(currentProfile, updatedProfile);

        assertNotNull(result);
        assertEquals("Новое описание", result.getDescription());
        assertEquals("12.01.2011", result.getBirthday());
        assertEquals("Италия", result.getCountry());
        assertEquals("Рим", result.getCity());
        assertEquals("555-5678", result.getPhone());
        verify(profileRepository, times(1)).findById(1L); /* проверяет, что метод findById()
        был вызван ровно 1 раз (times(1)) на объекте profileRepository. */

        verify(profileRepository, times(1)).save(eq(currentProfile));
    }

    @Test
    void testUpdateProfileWithInvalidBirthday() {

        currentProfile.setBirthday("11.10.2001");


        ProfileDTO updatedProfile = new ProfileDTO();
        updatedProfile.setBirthday("11.10.01"); // Неверный формат даты

        when(profileRepository
                .findById(1L))
                .thenReturn(Optional.of(currentProfile));


        assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateProfile(currentProfile, updatedProfile);
        });

    }

    @Test
    void testUpdateProfileWithNonExistentProfile() {

        ProfileDTO updatedProfile = new ProfileDTO();
        updatedProfile.setDescription("Новое описание");

        when(profileRepository
                .findById(1L))
                .thenReturn(Optional.empty()); // когда не нашелся профиль с указанным айди

        assertThrows(EntityNotFoundException.class, () -> {
            profileService.updateProfile(currentProfile, updatedProfile);
        });

        verify(profileRepository, times(0)).save(eq(currentProfile));


    }

    // тесты аватара
    @Test
    public void testUpdateAvatarWithValidData() {

        // Создаем MultipartFile с данными
        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        when(fileUploader
                .uploadFileOnServer(eq(avatar), anyString(), eq("avatars")))
                .thenReturn("expectedUrl");

        when(profileRepository
                .save(currentProfile))
                .thenAnswer(i -> i.getArguments()[0]);

        ProfileBom result = profileService.updateAvatar(avatar, currentProfile);

        assertNotNull(result);
        assertEquals("expectedUrl", result.getAvatar());

        verify(fileUploader, never()).deleteFileFromServer(anyString(), eq("avatars"));

    }

    @Test
    public void testUpdateAvatarWithInvalidData() {
        // Пустой MultipartFile
        byte[] bytes = new byte[0];
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", bytes);


        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));


        assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateAvatar(avatar, currentProfile);
        });

        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));

    }

    @Test
    public void testSuccessUpdateAvatarWithOldAvatar() {

        currentProfile.setAvatar("old-avatar");

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        doNothing().when(fileUploader) // из-за того что функция ничего не возвращает
                .deleteFileFromServer(eq("old-avatar"), eq("avatars"));

        when(fileUploader
                .uploadFileOnServer(eq(avatar), anyString(), eq("avatars")))
                .thenReturn("expectedUrl");

        when(profileRepository.save(currentProfile))
                .thenReturn(currentProfile);

        ProfileBom result = profileService.updateAvatar(avatar, currentProfile);

        assertEquals(expectedProfileBom.getAvatar(), result.getAvatar());
        assertNotNull(result);

        verify(fileUploader).deleteFileFromServer(eq("old-avatar"), eq("avatars"));
        verify(fileUploader).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));
        verify(profileRepository).save(currentProfile);

    }

    @Test
    public void testFailedDeleteOldAvatar() {

        currentProfile.setAvatar("old-avatar");

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        // Имитация ошибки при удалении файла на сервере
        doThrow(new RuntimeException("Ошибка при удалении старого аватара."))
                .when(fileUploader).deleteFileFromServer(anyString(), eq("avatars"));

        // Выполнение и проверка
        Exception exception = assertThrows(RuntimeException.class, () -> {
            profileService.updateAvatar(avatar, currentProfile);
        });

        assertTrue(exception.getMessage().contains("Ошибка при удалении старого аватара."));

        verify(fileUploader, times(1)).deleteFileFromServer(anyString(), eq("avatars"));
        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));

    }

    @Test
    public void testUpdateAvatarWithNonExistentProfile() {
        // Создаем MultipartFile с данными
        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            profileService.updateAvatar(avatar, currentProfile);
        });

        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).deleteFileFromServer(anyString(), eq("avatars"));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));

    }

    @Test
    public void testFailedUploadAvatar() {

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile avatar = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setAvatar("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        // Имитация ошибки при удалении файла на сервере
        doThrow(new RuntimeException("Ошибка при добавлении аватара."))
                .when(fileUploader).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));

        // Выполнение и проверка
        Exception exception = assertThrows(RuntimeException.class, () -> {
            profileService.updateAvatar(avatar, currentProfile);
        });

        assertTrue(exception.getMessage().contains("Ошибка при обновлении аватара."));

        verify(fileUploader, times(0)).deleteFileFromServer(anyString(), eq("avatars"));
        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(1)).uploadFileOnServer(eq(avatar), anyString(), eq("avatars"));

    }

    // тесты баннера
    @Test
    public void testUpdateBannerWithValidData() {

        // Создаем MultipartFile с данными
        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        when(fileUploader
                .uploadFileOnServer(eq(banner), anyString(), eq("banners")))
                .thenReturn("expectedUrl");

        when(profileRepository
                .save(currentProfile))
                .thenAnswer(i -> i.getArguments()[0]);

        ProfileBom result = profileService.updateBanner(banner, currentProfile);

        assertNotNull(result);
        assertEquals("expectedUrl", result.getBanner());

        verify(fileUploader, never()).deleteFileFromServer(anyString(), eq("banners"));

    }

    @Test
    public void testUpdateBannerWithInvalidData() {
        // Пустой MultipartFile
        byte[] bytes = new byte[0];
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", bytes);


        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));


        assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateBanner(banner, currentProfile);
        });

        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(banner), anyString(), eq("banners"));

    }

    @Test
    public void testSuccessUpdateBannerWithOldBanner() {

        currentProfile.setBanner("old-banner");

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        doNothing().when(fileUploader) // из-за того что функция ничего не возвращает
                .deleteFileFromServer(eq("old-banner"), eq("banners"));

        when(fileUploader
                .uploadFileOnServer(eq(banner), anyString(), eq("banners")))
                .thenReturn("expectedUrl");

        when(profileRepository.save(currentProfile))
                .thenReturn(currentProfile);

        ProfileBom result = profileService.updateBanner(banner, currentProfile);

        assertEquals(expectedProfileBom.getBanner(), result.getBanner());
        assertNotNull(result);

        verify(fileUploader).deleteFileFromServer(eq("old-banner"), eq("banners"));
        verify(fileUploader).uploadFileOnServer(eq(banner), anyString(), eq("banners"));
        verify(profileRepository).save(currentProfile);

    }

    @Test
    public void testFailedDeleteOldBanner() {

        currentProfile.setBanner("old-banner");

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        // Имитация ошибки при удалении файла на сервере
        doThrow(new RuntimeException("Ошибка при удалении старого баннера."))
                .when(fileUploader).deleteFileFromServer(anyString(), eq("banners"));

        // Выполнение и проверка
        Exception exception = assertThrows(RuntimeException.class, () -> {
            profileService.updateBanner(banner, currentProfile);
        });

        assertTrue(exception.getMessage().contains("Ошибка при удалении старого баннера."));

        verify(fileUploader, times(1)).deleteFileFromServer(anyString(), eq("banners"));
        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(banner), anyString(), eq("banners"));

    }

    @Test
    public void testUpdateBannerWithNonExistentProfile() {
        // Создаем MultipartFile с данными
        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            profileService.updateBanner(banner, currentProfile);
        });

        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(0)).deleteFileFromServer(anyString(), eq("banners"));
        verify(fileUploader, times(0)).uploadFileOnServer(eq(banner), anyString(), eq("banners"));

    }

    @Test
    public void testFailedUploadBanner() {

        byte[] imageData = {1, 2, 3, 4, 5};
        MultipartFile banner = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);

        ProfileBom expectedProfileBom = new ProfileBom();
        expectedProfileBom.setBanner("expectedUrl");

        when(profileRepository
                .findById(currentProfile.getId()))
                .thenReturn(Optional.of(currentProfile));

        // Имитация ошибки при удалении файла на сервере
        doThrow(new RuntimeException("Ошибка при добавлении баннера."))
                .when(fileUploader).uploadFileOnServer(eq(banner), anyString(), eq("banners"));

        // Выполнение и проверка
        Exception exception = assertThrows(RuntimeException.class, () -> {
            profileService.updateBanner(banner, currentProfile);
        });

        assertTrue(exception.getMessage().contains("Ошибка при обновлении баннера."));

        verify(fileUploader, times(0)).deleteFileFromServer(anyString(), eq("banners"));
        verify(profileRepository, times(0)).save(eq(currentProfile));
        verify(fileUploader, times(1)).uploadFileOnServer(eq(banner), anyString(), eq("banners"));

    }

}
