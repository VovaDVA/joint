package com.jointAuth.bom.user;

import com.jointAuth.interfaces.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse implements BaseResponse {
    private String token;

    @Override
    public String getMessage() {
        return null;
    }
}
