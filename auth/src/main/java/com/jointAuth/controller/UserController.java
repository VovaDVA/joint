package com.jointAuth.controller;

import com.jointAuth.bom.user.*;
import com.jointAuth.interfaces.BaseResponse;
import com.jointAuth.model.user.*;
import com.jointAuth.converter.UserConverter;
import com.jointAuth.model.verification.ConfirmAccountDeletionRequest;
import com.jointAuth.model.verification.ConfirmPasswordChangeRequest;
import com.jointAuth.model.verification.ConfirmPasswordResetRequest;
import com.jointAuth.bom.user.VerifyCodeRequest;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.service.VerificationCodeService;
import com.jointAuth.util.JwtTokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
        try {
            User registeredUser = userService.register(user);

            UserDTO userDTO = UserConverter.convertToDto(registeredUser);

            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Произошла ошибка при регистрации пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            if (user.getTwoFactorVerified()) {
                LoginResponse response = new LoginResponse(true, null);
                return ResponseEntity.ok(response);
            } else {
                String token = jwtTokenUtils.generateToken(user);
                LoginResponse response = new LoginResponse(false, token);
                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping(path = "/verify-code")
    public ResponseEntity<BaseResponse> verifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        try {
            Optional<User> userOptional = userService.findUserByCode(verifyCodeRequest.getCode());
            if (userOptional.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден по коду: " + verifyCodeRequest.getCode());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            User user = userOptional.get();
            String token = jwtTokenUtils.generateToken(user);
            JwtResponse jwtResponse = new JwtResponse(token);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/two-factor/enable")
    public ResponseEntity<BaseResponse> enableTwoFactorAuth(@RequestHeader("Authorization") String token) {
        try {
            Long currentUserId = jwtTokenUtils.getCurrentUserId(token);

            userService.enableTwoFactorAuth(currentUserId);

            ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация успешно включена");
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Двухфакторная аутентификация уже включена")) {
                ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация уже включена");
                return ResponseEntity.ok(apiResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
    }

    @PostMapping(path = "/two-factor/disable")
    public ResponseEntity<BaseResponse> disableTwoFactorAuth(@RequestHeader("Authorization") String token) {
        try {
            Long currentUserId = jwtTokenUtils.getCurrentUserId(token);

            userService.disableTwoFactorAuth(currentUserId);

            ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация успешно отключена");
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Двухфакторная аутентификация уже отключена")) {
                ApiResponse apiResponse = new ApiResponse("Двухфакторная аутентификация уже отключена");
                return ResponseEntity.ok(apiResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
    }

    @PostMapping(path = "/request-reset-password")
    public ResponseEntity<BaseResponse> requestPasswordReset(@RequestParam("email") String email) {
        Optional<User> currentUser = userService.getUserByEmail(email);

        if (currentUser.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь с таким email не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        boolean emailSent = userService.sendPasswordResetRequest(currentUser.get().getEmail());
        if (emailSent) {
            String maskedEmail = userService.maskEmail(currentUser.get().getEmail());
            ApiResponse successResponse = new ApiResponse("Код отправлен на email: " + maskedEmail);
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Не удалось отправить запрос на сброс пароля");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/confirm-reset-password")
    public ResponseEntity<BaseResponse> confirmPasswordReset(@RequestBody ConfirmPasswordResetRequest request) {
        try {
            if (userService.isPasswordValid(request.getNewPassword())) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Пароль не соответствует требованиям");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            boolean passwordReset = userService.resetPassword(request.getVerificationCode(), request.getNewPassword());

            if (passwordReset) {
                ApiResponse successResponse = new ApiResponse("Успешный сброс пароля");
                return ResponseEntity.ok(successResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Неверный проверочный код или не удалось сбросить пароль");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (IllegalArgumentException | AuthenticationException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Неверный проверочный код");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/user")
    public ResponseEntity<?> getUserByIdWithToken(@RequestHeader("Authorization") String token) {

        try {
            Long currentUserId = jwtTokenUtils.getCurrentUserId(token);
            UserProfileBom userResponseDTO = userService.getUserInfoById(currentUserId);

            return ResponseEntity.ok(userResponseDTO);
        } catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

    }

    @GetMapping("/user/get")
    public ResponseEntity<?> getUserDetailsById(@RequestParam Long userId) {
        try {
            UserBom userDetailsDTO = userService.getUserByIdWithoutToken(userId);

            if (userDetailsDTO != null) {
                return ResponseEntity.ok(userDetailsDTO);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
    public ResponseEntity<?> requestPasswordChange(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenUtils.getCurrentUserId(token);

        if (userId == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        boolean emailSent = userService.sendPasswordChangeRequest(userId);
        if (emailSent) {
            ApiResponse apiResponse = new ApiResponse("Запрос на изменение пароля отправлен на электронную почту");
            return ResponseEntity.ok(apiResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Не удалось отправить запрос на изменение пароля");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/confirm-change-password")
    public ResponseEntity<BaseResponse> confirmPasswordChange(@RequestBody ConfirmPasswordChangeRequest confirmPasswordChangeRequest) {
        try {
            boolean passwordChanged = userService.changePassword(
                    confirmPasswordChangeRequest.getUserId(),
                    confirmPasswordChangeRequest.getVerificationCode(),
                    confirmPasswordChangeRequest.getNewPassword(),
                    confirmPasswordChangeRequest.getCurrentPassword()
            );

            if (passwordChanged) {
                ApiResponse apiResponse = new ApiResponse("Пароль успешно изменен");
                return ResponseEntity.ok(apiResponse);
            } else {
                // Если изменение пароля не удалось, возвращаем HTTP-ответ 401 (UNAUTHORIZED)
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Не удалось изменить пароль");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (IllegalArgumentException e) {
            // Обработка `IllegalArgumentException` и возврат HTTP-ответа с соответствующим кодом и сообщением
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Обработка любых других исключений и возврат HTTP-ответа 500 (INTERNAL SERVER ERROR)
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> requestAccountDeletion(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenUtils.getCurrentUserId(token);

        if (userId == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        boolean emailSent = userService.sendAccountDeletionRequest(userId);

        if (emailSent) {
            ApiResponse apiResponse = new ApiResponse("Запрос на удаление аккаунта отправлен на электронную почту");
            return ResponseEntity.ok(apiResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Не удалось отправить запрос на удаление аккаунта");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/confirm-delete")
    public ResponseEntity<?> confirmAccountDeletion(@RequestBody ConfirmAccountDeletionRequest confirmAccountDeletionRequest) {
        boolean accountDeleted = userService.deleteUser(
                confirmAccountDeletionRequest.getUserId(),
                confirmAccountDeletionRequest.getVerificationCode()
        );

        if (accountDeleted) {
            ApiResponse apiResponse = new ApiResponse("Аккаунт успешно удален");
            return ResponseEntity.ok(apiResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Неверный код подтверждения или не удалось удалить аккаунт");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}