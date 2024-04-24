package com.jointAuth.bom.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserProfileBom {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Date registrationDate;
    private Date lastLogin;

    private Long profileId;
    private String description;
    private String birthday;
    private String country;
    private String city;
    private String phone;
    private Date lastEdited;

}
