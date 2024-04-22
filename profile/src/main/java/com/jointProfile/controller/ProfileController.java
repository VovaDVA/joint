package com.jointProfile.controller;
/*контроллер отвечает за обработку входящих запросов от клиентов (обычно браузеров)
и координирует выполнение соответствующих действий */

//в контроллере вызов реализаций из сервиса и базовые проверки связанные с приходящим запросом

import com.jointProfile.entity.Profile;
import com.jointProfile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/update")

    public ResponseEntity<?> updateProfile(@RequestBody Profile updatedProfile) {

        Optional<Profile> optionalProfile = profileService.updateProfile(updatedProfile); /* результат обновления профиля
        оборачивается в Optional, чтобы можно было проверить наличие значения.*/

        Profile existingProfile = optionalProfile.orElseThrow(() -> /* Метод orElseThrow используется для получения значения
        из объекта Optional или выбрасывания исключения, если внутри Optional нет значения. */
                new RuntimeException("Profile not found for user ID: " + updatedProfile.getId()));

        if (existingProfile != null) {
            return ResponseEntity.ok(existingProfile); // возвращается ResponseEntity c HTTP статусом ok (200) и существующим профилем
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // возвращается ResponseEntity с HTTP статусом 404 Not Found и сообщение.
                    .body("Profile not found or could not be updated");
        }
    }


}



