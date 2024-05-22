package com.jointProfile.bom.user;

import com.jointProfile.interfaces.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements BaseResponse {
    private int code;
    private String message;
}