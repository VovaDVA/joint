package com.jointAuth.model.user;

import lombok.Data;

@Data
public class ConfirmPasswordResetRequest {
    private Long userId;
    private String verificationCode;
    private String newPassword;
    private String currentPassword;
}
