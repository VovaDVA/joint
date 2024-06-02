package com.jointProfile.service;

import com.jointProfile.converter.ProfileConverter;
import com.jointProfile.bom.profile.ProfileBom;
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
                new EntityNotFoundException("Профиль не найден"));

        if (updatedProfile.getDescription() != null) {
            currentUpdatedProfile.setDescription(updatedProfile.getDescription());
        }

        if (updatedProfile.getBirthday() != null) {
            String birthdayStr = updatedProfile.getBirthday();
            String pattern = "^\\d{2}\\.\\d{2}\\.\\d{4}$"; // регулярное выражение для определенного формата

            if (birthdayStr.matches(pattern)) {
                currentUpdatedProfile.setBirthday(birthdayStr);
            } else {
                throw new IllegalArgumentException("Дата рождения должна быть в формате dd.MM.yy");
            }
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

        currentUpdatedProfile.setLastEdited(new Date());

        Profiles finalUpdatedProfile = profileRepository.save(currentUpdatedProfile);

        return ProfileConverter.converterToBom(finalUpdatedProfile);
    }

    public ProfileBom updateAvatar(MultipartFile avatar, Profiles profile) {

        // создание нового экзепляра объекта внутри метода
        Profiles currentProfile = profileRepository.findById(profile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Профиль не найден"));


        // Проверка наличия загруженного файла
        if (avatar == null || avatar.isEmpty()) {
            throw new IllegalArgumentException("Файл аватара отсутствует или пуст.");
        }

        // Проверка, есть ли в профиле старая аватарка
        String theOldAvatar = currentProfile.getAvatar();

        try {
            if (theOldAvatar != null && !theOldAvatar.isEmpty()) {
                fileUploader.deleteFileFromServer(theOldAvatar, "avatars");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении старого аватара.", e);
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
            throw new RuntimeException("Ошибка при обновлении аватара.", e);
        }

    }

    public ProfileBom updateBanner(MultipartFile banner, Profiles profile) {
        // создание нового экзепляра объекта внутри метода
        Profiles currentProfile = profileRepository.findById(profile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Профиль не найден."));

        // Проверка наличия загруженного файла
        if (banner == null || banner.isEmpty()) {
            throw new IllegalArgumentException("Файл баннера отсутствует или пуст.");
        }

        // Проверка, есть ли в профиле старый баннер
        String theOldBanner = currentProfile.getBanner();

        try {
            if (theOldBanner != null && !theOldBanner.isEmpty()) {
                fileUploader.deleteFileFromServer(theOldBanner, "banners");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении старого баннера.", e);
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
            throw new RuntimeException("Ошибка при обновлении баннера.", e);
        }

    }

}