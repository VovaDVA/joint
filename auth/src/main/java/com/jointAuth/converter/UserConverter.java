package com.jointAuth.converter;

import com.jointAuth.model.user.User;
import com.jointAuth.model.user.UserDTO;

public class UserConverter {

    public static UserDTO convertToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setLastLogin(user.getLastLogin());
        dto.setTwoFactorVerified(user.getTwoFactorVerified());
        return dto;
    }
}
