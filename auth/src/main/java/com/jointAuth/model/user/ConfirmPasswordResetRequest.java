package com.jointAuth.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPasswordResetRequest {
    private Long userId;
    private String verificationCode;
    private String newPassword;
    private String currentPassword;
}
