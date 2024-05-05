package com.jointAuth.bom.user;

import com.jointAuth.interfaces.BaseResponse;
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
