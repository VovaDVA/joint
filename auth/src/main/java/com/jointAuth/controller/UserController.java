package com.jointAuth.controller;

import com.jointAuth.model.JwtResponse;
import com.jointAuth.model.LoginRequest;
import com.jointAuth.model.User;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return ResponseEntity.ok(registeredUser);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
    }

    //Вход
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(),loginRequest.getPassword());

        if (user != null && userService.passwordsMatch(user.getPassword(), loginRequest.getPassword())) {
            String token = jwtTokenUtils.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    // Получение пользователя по ID
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Получение всех пользователей
    @GetMapping(path = "/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Обновление пароля
    @PutMapping(path = "/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestParam String password) {
        try {
            userService.changePassword(id, password);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Удаление
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);

        if (existingUser.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }


}