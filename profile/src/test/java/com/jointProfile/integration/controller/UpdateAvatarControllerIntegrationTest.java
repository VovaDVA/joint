package com.jointProfile.integration.controller;

import com.jointProfile.bom.profile.ProfileBom;
import com.jointProfile.connector.AuthConnector;
import com.jointProfile.entity.Profiles;
import com.jointProfile.service.ProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UpdateAvatarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthConnector authConnector;

    @MockBean
    private ProfileService profileService;


    @Test
    void testUpdateAvatarSuccess() throws Exception {

        String token = "valid_token";

        byte[] imageData = {1, 2, 3, 4, 5};
        MockMultipartFile avatar = new MockMultipartFile("avatar", "test.jpg", "image/jpeg", imageData);

        // Настройка поведения Mock-объектов
        when(authConnector
                .getCurrentProfile(token))
                .thenReturn(new ProfileBom());

        when(profileService
                .updateAvatar(eq(avatar), any(Profiles.class)))
                .thenReturn(new ProfileBom());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/profile/update-avatar")
                        .file(avatar)
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, times(1)).updateAvatar(eq(avatar), any(Profiles.class));
    }

    @Test
    void testUpdateAvatarRuntimeException() throws Exception {

        String token = "valid_token";

        byte[] imageData = {1, 2, 3, 4, 5};
        MockMultipartFile avatar = new MockMultipartFile("avatar", "test.jpg", "image/jpeg", imageData);

        // Настройка поведения Mock-объектов
        when(authConnector
                .getCurrentProfile(token))
                .thenThrow(new RuntimeException("Профиль не найден"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/profile/update-avatar")
                        .file(avatar)
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Профиль не найден"));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, never()).updateAvatar(eq(avatar), any(Profiles.class));

    }

    @Test
    void testUpdateAvatarWithInvalidToken() throws Exception {

        String token = "invalid_token";

        byte[] imageData = {1, 2, 3, 4, 5};
        MockMultipartFile avatar = new MockMultipartFile("avatar", "test.jpg", "image/jpeg", imageData);

        // Настройка поведения Mock-объектов
        when(authConnector
                .getCurrentProfile(token))
                .thenThrow(new RuntimeException("Профиль не найден"));


        mockMvc.perform(MockMvcRequestBuilders.multipart("/profile/update-avatar")
                        .file(avatar)
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Профиль не найден"));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, never()).updateAvatar(eq(avatar), any(Profiles.class));

    }

}

