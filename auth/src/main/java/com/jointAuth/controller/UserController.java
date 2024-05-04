package com.jointAuth.controller;

import com.jointAuth.bom.user.*;
import com.jointAuth.interfaces.BaseResponse;
import com.jointAuth.model.user.*;
import com.jointAuth.converter.UserConverter;
import com.jointAuth.model.verification.ConfirmAccountDeletionRequest;
import com.jointAuth.model.verification.ConfirmPasswordChangeRequest;
import com.jointAuth.model.verification.ConfirmPasswordResetRequest;
import com.jointAuth.model.verification.VerifyCodeRequest;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.service.VerificationCodeService;
import com.jointAuth.util.JwtTokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "auth")
public class UserController {

    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;

    private final VerificationCodeService verificationCodeService;

    private final TwoFactorAuthVerificationCodeRepository verificationCodeRepository;

    public UserController(@Autowired UserService userService,
                          @Autowired JwtTokenUtils jwtTokenUtils,
                          @Autowired VerificationCodeService verificationCodeService,
                          @Autowired TwoFactorAuthVerificationCodeRepository verificationCodeRepository) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.verificationCodeService = verificationCodeService;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.register(user);

        if (registeredUser != null) {
            UserDTO userDTO = UserConverter.convertToDto(registeredUser);
            return ResponseEntity.ok(userDTO);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не удалось зарегистрировать пользователя");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null && userService.passwordsMatch(user.getPassword(), loginRequest.getPassword())) {
            if (user.getTwoFactorVerified()) {
                LoginResponse response = new LoginResponse(true, null);
                return ResponseEntity.ok(response);
            } else {
                String token = jwtTokenUtils.generateToken(user);
                LoginResponse response = new LoginResponse(false, token);
                return ResponseEntity.ok(response);
            }
        }

        ApiResponse errorMessage = new ApiResponse("Неверный email или пароль");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @PostMapping(path = "/verify-code")
    public ResponseEntity<BaseResponse> verifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        boolean isValid = verificationCodeService.verifyVerificationCodeFor2FA(verifyCodeRequest.getUserId(), verifyCodeRequest.getCode());

        if (isValid) {
            User user = userService.getUserById(verifyCodeRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден по userId: " + verifyCodeRequest.getUserId()));

            String token = jwtTokenUtils.generateToken(user);
            JwtResponse jwtResponse = new JwtResponse(token);
            return ResponseEntity.ok(jwtResponse);
        } else if (verifyCodeRequest.getUserId() == null || verifyCodeRequest.getCode() == null) {
            ApiResponse apiResponse = new ApiResponse("Отсутствует код подтверждения или userId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } else {
            ApiResponse apiResponse = new ApiResponse("Неверный код подтверждения");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    @PostMapping(path = "/two-factor/enable")
    public ResponseEntity<ApiResponse> enableTwoFactorAuth(@RequestHeader("Authorization") String token) {
        try {
            Long currentUserId = jwtTokenUtils.getCurrentUserId(token);
            userService.enableTwoFactorAuth(currentUserId);
            ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация успешно включена");
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse apiResponse;
            if (e.getMessage().equals("Двухфакторная аутентификация уже включена")) {
                apiResponse = new ApiResponse("Двухфакторная аутентификация уже включена");
                return ResponseEntity.ok(apiResponse);
            } else {
                apiResponse = new ApiResponse("Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        }
    }

    @PostMapping(path = "/two-factor/disable")
    public ResponseEntity<ApiResponse> disableTwoFactorAuth(@RequestHeader("Authorization") String token) {
        try {
            Long currentUserId = jwtTokenUtils.getCurrentUserId(token);
            userService.disableTwoFactorAuth(currentUserId);
            ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация успешно отключена");
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse apiResponse;
            if (e.getMessage().equals("Двухфакторная аутентификация уже отключена")) {
                apiResponse = new ApiResponse(e.getMessage());
                return ResponseEntity.ok(apiResponse);
            } else {
                apiResponse = new ApiResponse("Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        }
    }


    @PostMapping(path = "/request-reset-password")
    public ResponseEntity<?> requestPasswordReset(@RequestParam("email") String email) {
        Optional<User> currentUser = userService.getUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь с таким email не найден");
        }

        boolean emailSent = userService.sendPasswordResetRequest(currentUser.get().getEmail());
        if (emailSent) {
            String maskedEmail = userService.maskEmail(currentUser.get().getEmail());
            return ResponseEntity.ok("Код отправлен на email: " + maskedEmail);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не удалось отправить запрос на сброс пароля");
        }
    }

    @PostMapping(path = "/confirm-reset-password")
    public ResponseEntity<?> confirmPasswordReset(@RequestBody ConfirmPasswordResetRequest confirmPasswordResetRequest) {
        boolean passwordReset = userService.resetPassword(confirmPasswordResetRequest.getVerificationCode(),
                confirmPasswordResetRequest.getNewPassword());

        if (passwordReset) {
            return ResponseEntity.ok("Успешный сброс пароля");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный проверочный код или не удалось сбросить пароль");
        }
    }


    @GetMapping(path = "/user")
    public ResponseEntity<UserProfileBom> getUserByIdWithToken(@RequestHeader("Authorization") String token) {
        Long currentUserId = jwtTokenUtils.getCurrentUserId(token);
        UserProfileBom userResponseDTO = userService.getUserInfoById(currentUserId);

        if (userResponseDTO != null) {
            return ResponseEntity.ok(userResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/get")
    public ResponseEntity<UserBom> getUserDetailsById(@RequestParam Long userId) {
        UserBom userDetailsDTO = userService.getUserByIdWithoutToken(userId);

        if (userDetailsDTO != null) {
            return ResponseEntity.ok(userDetailsDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<UserDTO> userDTOs = users.stream()
                    .map(user -> {
                        UserDTO dto = new UserDTO();
                        BeanUtils.copyProperties(user, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<ApiResponse> requestPasswordChange(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenUtils.getCurrentUserId(token);

        if (userId == null) {
            ApiResponse apiResponse = new ApiResponse("Пользователь не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        boolean emailSent = userService.sendPasswordChangeRequest(userId);
        if (emailSent) {
            ApiResponse apiResponse = new ApiResponse("Запрос на изменение пароля отправлен на электронную почту");
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse apiResponse = new ApiResponse("Не удалось отправить запрос на изменение пароля");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }


    @PostMapping(path = "/confirm-change-password")
    public ResponseEntity<ApiResponse> confirmPasswordChange(@RequestBody ConfirmPasswordChangeRequest confirmPasswordChangeRequest) {
        boolean passwordReset = userService.changePassword(
                confirmPasswordChangeRequest.getUserId(),
                confirmPasswordChangeRequest.getVerificationCode(),
                confirmPasswordChangeRequest.getNewPassword(),
                confirmPasswordChangeRequest.getCurrentPassword()
        );

        if (passwordReset) {
            ApiResponse apiResponse = new ApiResponse("Пароль успешно изменен");
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse apiResponse = new ApiResponse("Неверный код подтверждения");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<ApiResponse> requestAccountDeletion(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenUtils.getCurrentUserId(token);

        if (userId == null) {
            ApiResponse apiResponse = new ApiResponse("Пользователь не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        boolean emailSent = userService.sendAccountDeletionRequest(userId);
        if (emailSent) {
            ApiResponse apiResponse = new ApiResponse("Запрос на удаление аккаунта отправлен на электронную почту");
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse apiResponse = new ApiResponse("Не удалось отправить запрос на удаление аккаунта");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }


    @DeleteMapping(path = "/confirm-delete")
    public ResponseEntity<ApiResponse> confirmAccountDeletion(@RequestBody ConfirmAccountDeletionRequest confirmAccountDeletionRequest) {
        boolean accountDeleted = userService.deleteUser(
                confirmAccountDeletionRequest.getUserId(),
                confirmAccountDeletionRequest.getVerificationCode()
        );

        if (accountDeleted) {
            ApiResponse apiResponse = new ApiResponse("Аккаунт успешно удален");
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse apiResponse = new ApiResponse("Неверный код подтверждения или не удалось удалить аккаунт");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

}