package com.jointProfile.integration.controller;

import com.jointProfile.bom.profile.ProfileBom;
import com.jointProfile.connector.AuthConnector;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.service.ProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UpdateProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthConnector authConnector;

    @MockBean
    private ProfileService profileService;

    @Test
    void testUpdateProfileSuccess() throws Exception {

        String token = "valid_token";

        // Настройка поведения Mock-объектов
        when(authConnector
                .getCurrentProfile(token))
                .thenReturn(new ProfileBom());


        when(profileService
                .updateProfile(any(Profiles.class), any(ProfileDTO.class)))
                .thenReturn(new ProfileBom());

        String updateRequestJson = "{\n" +
                "  \"description\": \"Описание\",\n" +
                "  \"birthday\": \"22.01.2004\",\n" +
                "  \"country\": \"США\",\n" +
                "  \"city\": \"Нью-Йорк\",\n" +
                "  \"phone\": \"12345678\"\n" +
                "}";

        // Выполнение тестового запроса
        mockMvc.perform(put("/profile/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk());

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, times(1)).updateProfile(any(Profiles.class), any(ProfileDTO.class));
    }

    @Test
    void testUpdateProfileRuntimeException() throws Exception {

        String token = "valid_token";

        when(authConnector.getCurrentProfile(token))
                .thenThrow(new RuntimeException("Профиль не найден"));

        String updateRequestJson = "{\n" +
                "  \"description\": \"Описание\",\n" +
                "  \"birthday\": \"22.01.2004\",\n" +
                "  \"country\": \"США\",\n" +
                "  \"city\": \"Нью-Йорк\",\n" +
                "  \"phone\": \"12345678\"\n" +
                "}";

        mockMvc.perform(put("/profile/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Профиль не найден"));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, never()).updateProfile(any(Profiles.class), any(ProfileDTO.class));

    }

    @Test
    void testUpdateProfileWithInvalidToken() throws Exception {

        String token = "invalid_token";

        // Настройка поведения Mock-объектов
        when(authConnector
                .getCurrentProfile(token))
                .thenThrow(new RuntimeException("Профиль не найден"));

        String updateRequestJson = "{\n" +
                "  \"description\": \"Описание\",\n" +
                "  \"birthday\": \"22.01.2004\",\n" +
                "  \"country\": \"США\",\n" +
                "  \"city\": \"Нью-Йорк\",\n" +
                "  \"phone\": \"12345678\"\n" +
                "}";

        mockMvc.perform(put("/profile/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Профиль не найден"));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, never()).updateProfile(any(Profiles.class), any(ProfileDTO.class));

    }

    @Test
    void testUpdateProfileWithInvalidDateFormat() throws Exception {
        String token = "valid_token";

        // Настройка поведения Mock-объектов, чтобы они генерировали RuntimeException
        when(authConnector.getCurrentProfile(token))
                .thenReturn(new ProfileBom());

        when(profileService.updateProfile(any(Profiles.class), any(ProfileDTO.class)))
                .thenThrow(new RuntimeException("Профиль не найден"));

        String updateRequestJson = "{\n" +
                "  \"description\": \"Описание\",\n" +
                "  \"birthday\": \"invalid_date\",\n" +
                "  \"country\": \"США\",\n" +
                "  \"city\": \"Нью-Йорк\",\n" +
                "  \"phone\": \"12345678\"\n" +
                "}";

        // Выполнение тестового запроса
        mockMvc.perform(put("/profile/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Профиль не найден"));

        // Проверка ожидаемых результатов
        verify(authConnector, times(1)).getCurrentProfile(token);
        verify(profileService, times(1)).updateProfile(any(Profiles.class), any(ProfileDTO.class));
    }

}


