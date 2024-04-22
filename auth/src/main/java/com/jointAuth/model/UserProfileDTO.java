package com.jointAuth.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class UserProfileDTO {
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
