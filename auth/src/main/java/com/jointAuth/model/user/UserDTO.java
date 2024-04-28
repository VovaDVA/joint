package com.jointAuth.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date registrationDate;
    private Date lastLogin;
    private Boolean twoFactorVerified;
}
