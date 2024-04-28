package com.jointAuth.model.user;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private Long userId;
    private String code;
}
