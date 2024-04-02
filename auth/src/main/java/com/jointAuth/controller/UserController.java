package com.jointAuth.controller;

import com.jointAuth.model.JwtResponse;
import com.jointAuth.model.LoginRequest;
import com.jointAuth.model.User;
import com.jointAuth.model.UserDTO;
import com.jointAuth.service.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    //Регистрация
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.register(user);

        if (registeredUser != null) {
            UserDTO userDTO = userService.convertToDto(registeredUser);
            return ResponseEntity.ok(userDTO);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
    }

    //Вход
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null && userService.passwordsMatch(user.getPassword(), loginRequest.getPassword())) {
            String token = jwtTokenUtils.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    // Получение пользователя по ID
    @GetMapping(path = "/get-id")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String token) {

        Long currentUserId = jwtTokenUtils.getCurrentUserId(token);

        Optional<User> user = userService.getUserById(currentUserId);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    BeanUtils.copyProperties(user, dto); // или любой другой метод копирования свойств
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    //Обновление пароля
    @PutMapping(path = "/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody String password) {

        Long currentUserId = jwtTokenUtils.getCurrentUserId(token);

        Optional<User> existingUser = userService.getUserById(currentUserId);

        if (existingUser.isPresent()) {
            userService.changePassword(currentUserId, password);
            return ResponseEntity.ok("Password changed successfully");
        }

        return ResponseEntity.notFound().build();

    }

    //Удаление
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {

        Long currentUserId = jwtTokenUtils.getCurrentUserId(token);

        Optional<User> existingUser = userService.getUserById(currentUserId);

        if (existingUser.isPresent()) {
            userService.deleteUser(currentUserId);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}