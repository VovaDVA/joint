package com.jointAuth.bom.user;

import com.jointAuth.interfaces.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements BaseResponse {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}
