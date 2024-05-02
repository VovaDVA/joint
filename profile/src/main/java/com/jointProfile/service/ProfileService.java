package com.jointProfile.service;

import com.jointProfile.converter.ProfileConverter;
import com.jointProfile.bom.ProfileBom;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.repository.ProfileRepository;
import com.jointProfile.utils.RemoteFileUploader;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RemoteFileUploader fileUploader;


    public ProfileBom updateProfile(Profiles currentProfile, ProfileDTO updatedProfile) {

        Profiles currentUpdatedProfile = profileRepository.findById(currentProfile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Profile not found"));

        if (updatedProfile.getDescription() != null) {
            currentUpdatedProfile.setDescription(updatedProfile.getDescription());
        }
        if (updatedProfile.getBirthday() != null) {
            currentUpdatedProfile.setBirthday(updatedProfile.getBirthday());
        }
        if (updatedProfile.getCountry() != null) {
            currentUpdatedProfile.setCountry(updatedProfile.getCountry());
        }
        if (updatedProfile.getCity() != null) {
            currentUpdatedProfile.setCity(updatedProfile.getCity());
        }
        if (updatedProfile.getPhone() != null) {
            currentUpdatedProfile.setPhone(updatedProfile.getPhone());
        }

        currentProfile.setLastEdited(new Date());

        profileRepository.save(currentProfile);

        return ProfileConverter.converterToBom(currentProfile);
    }

    public ProfileBom updateAvatar(MultipartFile avatar, Profiles profile) {

        // создание нового экзепляра объекта внутри метода
        Profiles currentProfile = profileRepository.findById(profile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Profile not found"));


        // Проверка наличия загруженного файла
        if (avatar == null || avatar.isEmpty()) {
            throw new IllegalArgumentException("Avatar file is missing or empty.");
        }

        String fileNameOnServer = UUID.randomUUID() + ".jpg"; // каждый файл имеет свой UUID

        try {
            // Загрузка аватара на сервер
            String url = fileUploader.uploadFileOnServer(avatar, fileNameOnServer, "avatars");
            currentProfile.setAvatar(url);

            // Сохранение обновленного профиля в репозитории
            profileRepository.save(currentProfile);

            return ProfileConverter.converterToBom(currentProfile);
        } catch (Exception e) {
            // Обработка других непредвиденных исключений
            throw new RuntimeException("An error occurred while updating avatar.", e);
        }

    }

    public ProfileBom updateBanner(MultipartFile banner, Profiles profile) {
        // создание нового экзепляра объекта внутри метода
        Profiles currentProfile = profileRepository.findById(profile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Profile not found"));

        // Проверка наличия загруженного файла
        if (banner == null || banner.isEmpty()) {
            throw new IllegalArgumentException("Banner file is missing or empty.");
        }

        String fileNameOnServer = UUID.randomUUID() + ".jpg";

        try {
            // Загрузка аватара на сервер
            String url = fileUploader.uploadFileOnServer(banner, fileNameOnServer, "banners");
            currentProfile.setBanner(url);

            // Сохранение обновленного профиля в репозитории
            profileRepository.save(currentProfile);

            return ProfileConverter.converterToBom(currentProfile);
        } catch (Exception e) {
            // Обработка других непредвиденных исключений
            throw new RuntimeException("An error occurred while updating banner.", e);
        }

    }



}