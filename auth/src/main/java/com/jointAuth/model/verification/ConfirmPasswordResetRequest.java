package com.jointAuth.model.verification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPasswordResetRequest {
    private String verificationCode;
    private String newPassword;
}
