package com.jointAuth.service;

import com.jointAuth.model.Profile;
import com.jointAuth.model.User;
import com.jointAuth.model.UserDetailsDTO;
import com.jointAuth.model.UserProfileDTO;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ProfileRepository profileRepository;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{8,}$";

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String NAME_REGEX = "^[a-zA-Z]+$";

    private static final int NAME_MAX_LENGTH = 15;

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired PasswordEncoder passwordEncoder,
                       @Autowired ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
    }


    public User register(User user) {
        validateName(user.getFirstName(), "First name");

        validateName(user.getLastName(), "Last name");

        String email = user.getEmail();
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        if (validatePassword(user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet the complexity requirements");
        }

        user.setRegistrationDate(new Date());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        createProfileForUser(savedUser);

        return savedUser;
    }

    private void createProfileForUser(User savedUser) {
        Profile profile= new Profile();

        profile.setUser(savedUser);
        profile.setLastEdited(new Date());
        profileRepository.save(profile);
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty or contain only whitespace");
        } else if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException(fieldName + " must contain only letters");
        } else if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(fieldName + " length must not exceed " + NAME_MAX_LENGTH + " characters");
        }
    }

    private boolean validatePassword(String password) {
        return !pattern.matcher(password).matches();
    }

    private  boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public User login(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Missing email.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Missing password.");
        }

        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            user.setLastLogin(new Date());
            userRepository.save(user);

            return user;
        }

        throw new IllegalArgumentException("Invalid email or password.");
    }

    public boolean passwordsMatch(String hashedPassword, String plainPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserProfileDTO getUserInfoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));

        UserProfileDTO userResponseDTO = new UserProfileDTO();
        userResponseDTO.setUserId(user.getId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRegistrationDate(user.getRegistrationDate());
        userResponseDTO.setLastLogin(user.getLastLogin());

        Profile userProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));

        userResponseDTO.setProfileId(userProfile.getId());
        userResponseDTO.setDescription(userProfile.getDescription());
        userResponseDTO.setBirthday(userProfile.getBirthday());
        userResponseDTO.setCountry(userProfile.getCountry());
        userResponseDTO.setCity(userProfile.getCity());
        userResponseDTO.setPhone(userProfile.getPhone());
        userResponseDTO.setLastEdited(userProfile.getLastEdited());

        return userResponseDTO;
    }

    public UserDetailsDTO getUserByIdWithoutToken(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
            userDetailsDTO.setFirstName(user.getFirstName());
            userDetailsDTO.setLastName(user.getLastName());
            userDetailsDTO.setLastLogin(user.getLastLogin());

            Optional<Profile> userProfileOptional = profileRepository.findByUserId(userId);
            if (userProfileOptional.isPresent()) {
                Profile userProfile = userProfileOptional.get();
                userDetailsDTO.setDescription(userProfile.getDescription());
                userDetailsDTO.setBirthday(userProfile.getBirthday());
                userDetailsDTO.setCountry(userProfile.getCountry());
                userDetailsDTO.setCity(userProfile.getCity());
            } else {
                throw new RuntimeException("Profile not found for userId: " + userId);
            }

            return userDetailsDTO;
        } else {
            throw new RuntimeException("User not found with userId: " + userId);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changePassword(Long userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (validatePassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet the complexity requirements");
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}