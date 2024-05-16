package com.jointAuth.bom.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserBom {
    private String firstName;
    private String lastName;

    private String description;
    private String birthday;
    private String country;
    private String city;
    private Date lastLogin;
    private String avatar;
    private String banner;
}
