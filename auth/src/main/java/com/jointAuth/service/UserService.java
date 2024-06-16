package com.jointAuth.service;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.verification.PasswordResetVerificationCode;
import com.jointAuth.model.verification.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.bom.user.UserBom;
import com.jointAuth.bom.user.UserProfileBom;
import com.jointAuth.model.verification.TwoFactorAuthVerificationCode;
import com.jointAuth.model.verification.UserVerificationCode;
import com.jointAuth.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository;

    private final TwoFactorAuthVerificationCodeRepository twoFactorAuthVerificationCodeRepository;

    private final String COMBINATIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,}$";

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String NAME_REGEX = "^[а-яА-ЯёЁ]+$";

    private static final int NAME_MAX_LENGTH = 20;

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired PasswordEncoder passwordEncoder,
                       @Autowired ProfileRepository profileRepository,
                       @Autowired VerificationCodeService verificationCodeService,
                       @Autowired EmailService emailService,
                       @Autowired UserVerificationCodeRepository userVerificationCodeRepository,
                       @Autowired PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository,
                       @Autowired TwoFactorAuthVerificationCodeRepository twoFactorAuthVerificationCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
        this.userVerificationCodeRepository = userVerificationCodeRepository;
        this.passwordResetVerificationCodeRepository = passwordResetVerificationCodeRepository;
        this.twoFactorAuthVerificationCodeRepository = twoFactorAuthVerificationCodeRepository;
    }


    public User register(User user) {
        validateName(user.getFirstName(), "Имя");

        validateName(user.getLastName(), "Фамилия");

        String email = user.getEmail();
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Неверный формат электронной почты");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        if (validatePassword(user.getPassword())) {
            throw new IllegalArgumentException("Пароль не соответствует требованиям безопасности");
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
            throw new IllegalArgumentException(fieldName + " не может состоять только из пробелов");
        } else if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException(fieldName + " может содержать только русские буквы");
        } else if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(fieldName + " не может превышать " + NAME_MAX_LENGTH + " символов");
        }
    }

    public boolean isPasswordValid(String password) {
        return validatePassword(password);
    }

    private boolean validatePassword(String password) {
        return !pattern.matcher(password).matches();
    }

    private  boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public User login(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Отсутствует email");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Отсутствует пароль");
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

        throw new IllegalArgumentException("Неверный email или пароль");
    }

    public void enableTwoFactorAuth(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getTwoFactorVerified()) {
                throw new IllegalArgumentException("Двухфакторная аутентификация уже включена");
            }

            user.setTwoFactorVerified(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }

    public void disableTwoFactorAuth(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (!user.getTwoFactorVerified()) {
                throw new IllegalArgumentException("Двухфакторная аутентификация уже отключена");
            }

            user.setTwoFactorVerified(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }


    public boolean passwordsMatch(String hashedPassword, String plainPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }

        StringBuilder maskedEmail = new StringBuilder(email);
        for (int i = 1;i < atIndex - 1; i++) {
            maskedEmail.setCharAt(i, '*');
        }

        return maskedEmail.toString();
    }

    public boolean sendPasswordResetRequest(String email) {
        Optional<User> optionalCurrentUser = Optional.ofNullable(userRepository.findByEmail(email));

        return optionalCurrentUser.map(currentUser -> {
            Long userId = currentUser.getId();
            String verificationCode = generateVerificationCode();
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

            verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(
                    userId, verificationCode, expirationTime
            );

            return emailService.sendPasswordResetConfirmationEmail(currentUser, verificationCode);
        }).orElse(false);
    }

    public Optional<User> findUserByCode(String code) {
        Optional<TwoFactorAuthVerificationCode> verificationCodeOptional = twoFactorAuthVerificationCodeRepository.findByCode(code);

        return verificationCodeOptional.map(TwoFactorAuthVerificationCode::getUser);
    }

    public boolean resetPassword(String verificationCode, String newPassword) {
        Optional<PasswordResetVerificationCode> optionalPasswordResetVerificationCode = passwordResetVerificationCodeRepository.findByCode(verificationCode);

        if (optionalPasswordResetVerificationCode.isPresent()) {
            PasswordResetVerificationCode passwordResetVerificationCode = optionalPasswordResetVerificationCode.get();
            Long userId = passwordResetVerificationCode.getUser().getId();

            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (passwordResetVerificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalArgumentException("Новый пароль не соответствует требованиям к сложности");
                }

                if (!validatePassword(newPassword)) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);

                    passwordResetVerificationCodeRepository.delete(passwordResetVerificationCode);

                    return true;
                } else {
                    throw new IllegalArgumentException("Новый пароль не соответствует требованиям к сложности");
                }
            } else {
                throw new IllegalArgumentException("Пользователь не найден");
            }
        } else {
            throw new IllegalArgumentException("Неверный код подтверждения");
        }
    }

    public UserProfileBom getUserInfoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден с userId: " + userId));

        UserProfileBom userResponseBom = new UserProfileBom();
        userResponseBom.setUserId(user.getId());
        userResponseBom.setFirstName(user.getFirstName());
        userResponseBom.setLastName(user.getLastName());
        userResponseBom.setEmail(user.getEmail());
        userResponseBom.setRegistrationDate(user.getRegistrationDate());
        userResponseBom.setLastLogin(user.getLastLogin());
        userResponseBom.setTwoFactorEnabled(user.getTwoFactorVerified());

        Profile userProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Профиль не найден для userId: " + userId));

        userResponseBom.setProfileId(userProfile.getId());
        userResponseBom.setDescription(userProfile.getDescription());
        userResponseBom.setBirthday(userProfile.getBirthday());
        userResponseBom.setCountry(userProfile.getCountry());
        userResponseBom.setCity(userProfile.getCity());
        userResponseBom.setPhone(userProfile.getPhone());
        userResponseBom.setAvatar(userProfile.getAvatar());
        userResponseBom.setBanner(userProfile.getBanner());
        userResponseBom.setLastEdited(userProfile.getLastEdited());

        return userResponseBom;
    }

    public UserBom getUserByIdWithoutToken(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserBom userDetailsBom = new UserBom();
            userDetailsBom.setFirstName(user.getFirstName());
            userDetailsBom.setLastName(user.getLastName());
            userDetailsBom.setLastLogin(user.getLastLogin());

            Optional<Profile> userProfileOptional = profileRepository.findByUserId(userId);
            if (userProfileOptional.isPresent()) {
                Profile userProfile = userProfileOptional.get();
                userDetailsBom.setDescription(userProfile.getDescription());
                userDetailsBom.setBirthday(userProfile.getBirthday());
                userDetailsBom.setCountry(userProfile.getCountry());
                userDetailsBom.setCity(userProfile.getCity());
                userDetailsBom.setAvatar(userProfile.getAvatar());
                userDetailsBom.setBanner(userProfile.getBanner());
            } else {
                throw new NoSuchElementException("Профиль не найден для userId: " + userId);
            }

            return userDetailsBom;
        } else {
            throw new NoSuchElementException("Пользователь не найден с userId: " + userId);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean changePassword(Long userId, String verificationCode, String newPassword, String currentPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<UserVerificationCode> optionalVerificationCode = userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode);

            if (optionalVerificationCode.isPresent()) {
                UserVerificationCode userVerificationCode = optionalVerificationCode.get();

                if (userVerificationCode.getRequestType() != RequestType.PASSWORD_CHANGE) {
                    throw new IllegalArgumentException("Неверный тип запроса для изменения пароля");
                }

                if (userVerificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalArgumentException("Код подтверждения истек");
                }

                if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                    throw new IllegalArgumentException("Неверный текущий пароль");
                }

                if (validatePassword(newPassword)) {
                    throw new IllegalArgumentException("Пароль не соответствует требованиям к сложности");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                userVerificationCodeRepository.delete(userVerificationCode);

                return true;
            } else {
                throw new IllegalArgumentException("Неверный код подтверждения");
            }
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }

    public String getUserEmailById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional == null || userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        return user.getEmail();
    }

    public boolean sendPasswordChangeRequest(Long userId) {
        String email = getUserEmailById(userId);

        if (email == null) {
            return false;
        }

        Optional<User> optionalCurrentUser = Optional.ofNullable(userRepository.findByEmail(email));

        return optionalCurrentUser.map(currentUser -> {
            String verificationCode = generateVerificationCode();
            RequestType requestType = RequestType.PASSWORD_CHANGE;
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

            verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(
                    userId, verificationCode, requestType, expirationTime
            );

            return emailService.sendPasswordChangeConfirmationEmail(currentUser, verificationCode);
        }).orElse(false);
    }

    public boolean sendAccountDeletionRequest(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        try {
            String verificationCode = generateVerificationCode();
            verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, verificationCode, LocalDateTime.now().plusMinutes(2));

            return emailService.sendAccountDeletionConfirmationEmail(user, verificationCode);
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean deleteUser(Long userId, String verificationCode) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        boolean isVerified = verificationCodeService.verifyUserVerificationCodeForUser(userId, verificationCode);

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