package com.jointAuth.service;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.bom.user.UserBom;
import com.jointAuth.bom.user.UserProfileBom;
import com.jointAuth.model.user.UserVerificationCode;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ProfileRepository profileRepository;

    private final VerificationCodeService verificationCodeService;

    private final EmailService emailService;

    private final UserVerificationCodeRepository userVerificationCodeRepository;

    private final String COMBINATIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,}$";

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String NAME_REGEX = "^[a-zA-Zа-яА-ЯёЁ]+$";

    private static final int NAME_MAX_LENGTH = 15;

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired PasswordEncoder passwordEncoder,
                       @Autowired ProfileRepository profileRepository,
                       @Autowired VerificationCodeService verificationCodeService,
                       @Autowired EmailService emailService,
                       @Autowired UserVerificationCodeRepository userVerificationCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
        this.userVerificationCodeRepository = userVerificationCodeRepository;
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
        Profile profile = new Profile();

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

            if (user.getTwoFactorVerified()) {
                String verificationCode = generateVerificationCode();
                verificationCodeService.saveOrUpdateVerificationCodeFor2FA(user.getId(), verificationCode);
                sendVerificationCode(user, verificationCode);
            }

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

    public UserProfileBom getUserInfoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));

        UserProfileBom userResponseDTO = new UserProfileBom();
        userResponseDTO.setUserId(user.getId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRegistrationDate(user.getRegistrationDate());
        userResponseDTO.setLastLogin(user.getLastLogin());
        userResponseDTO.setTwoFactorEnabled(user.getTwoFactorVerified());

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

    public UserBom getUserByIdWithoutToken(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserBom userDetailsDTO = new UserBom();
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

    public boolean resetPassword(Long userId, String verificationCode, String newPassword, String currentPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<UserVerificationCode> optionalVerificationCode = userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode);

            if (optionalVerificationCode.isPresent()) {
                UserVerificationCode userVerificationCode = optionalVerificationCode.get();

                if (userVerificationCode.getRequestType() != RequestType.PASSWORD_RESET) {
                    throw new IllegalArgumentException("Invalid request type for password reset");
                }

                if (userVerificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalArgumentException("Verification code has expired");
                }

                if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                    throw new IllegalArgumentException("Invalid current password");
                }

                if (validatePassword(newPassword)) {
                    throw new IllegalArgumentException("Password does not meet the complexity requirements");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                userVerificationCodeRepository.delete(userVerificationCode);

                return true;
            } else {
                throw new IllegalArgumentException("Invalid verification code");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public String getUserEmailById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getEmail();
        } else {
            return null;
        }
    }

    public boolean sendPasswordResetRequest(Long userId) {
        String email = getUserEmailById(userId);

        if (email == null) {
            return false;
        }

        User currentUser = userRepository.findByEmail(email);

        if (currentUser == null) {
            return false;
        }

        String verificationCode = generateVerificationCode();

        RequestType requestType = RequestType.PASSWORD_RESET;

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

        verificationCodeService.saveOrUpdateVerificationCodeForResetPassword(userId, verificationCode, requestType, expirationTime);

        return emailService.sendPasswordResetConfirmationEmail(currentUser, verificationCode);
    }

    public boolean sendAccountDeletionRequest(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        String verificationCode = generateVerificationCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

        verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, verificationCode, expirationTime);

        return emailService.sendAccountDeletionConfirmationEmail(user, verificationCode);
    }

    @Transactional
    public boolean deleteUser(Long userId, String verificationCode) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        boolean isVerified = verificationCodeService.verifyUserVerificationCode(userId, verificationCode);

        if (!isVerified) {
            return false;
        }

        deleteUserVerificationCodesByUserId(userId);

        deleteUserById(userId);

        return true;
    }

    @Transactional
    public void deleteUserVerificationCodesByUserId(Long userId) {
        userVerificationCodeRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        profileRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    public void enableTwoFactorAuth(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setTwoFactorVerified(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void disableTwoFactorAuth(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setTwoFactorVerified(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();

        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            code.append(COMBINATIONS.charAt(random.nextInt(COMBINATIONS.length())));
        }

        return code.toString();
    }

    public void sendVerificationCode(User user, String verificationCode) {
        emailService.sendVerificationCodeByEmail(user, verificationCode);
    }
}