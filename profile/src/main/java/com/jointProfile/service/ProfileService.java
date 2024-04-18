package com.jointProfile.service;
// реализуется основная логика функций

import com.jointProfile.entity.Profile;
import com.jointProfile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Optional<Profile> updateProfile(Profile updatedProfile) {

        Optional<Profile> existingProfile = profileRepository.findById(updatedProfile.getId());

        if (!isProfileExists(updatedProfile.getId(), updatedProfile.getUserId())) {
            throw new IllegalArgumentException("User ID in request does not match user ID in database");

        }

        if (existingProfile.isPresent()) {
            Profile profileToUpdate = getProfileToUpdate(updatedProfile, existingProfile);

            return Optional.of(profileRepository.save(profileToUpdate));
        }

        return Optional.empty();
    }

    private static Profile getProfileToUpdate(Profile updatedProfile, Optional<Profile> existingProfile) {
        Profile profileToUpdate = existingProfile.get();
        // Обновляем данные профиля
        profileToUpdate.setDescription(updatedProfile.getDescription());
        profileToUpdate.setBirthday(updatedProfile.getBirthday());
        profileToUpdate.setCountry(updatedProfile.getCountry());
        profileToUpdate.setCity(updatedProfile.getCity());
        profileToUpdate.setPhone(updatedProfile.getPhone());
        profileToUpdate.setLastEdited(new Date()); // Устанавливаем текущее время как время редактирования
        return profileToUpdate;
    }

    // функция чтобы проверить не пустой ли пользователь в случае, если id из БД и userID разные
    public boolean isProfileExists(Long id, Long userID) {
        return id.equals(userID);
    }

}
