package com.jointAuth.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailsDTO {
    private String firstName;
    private String lastName;

    private String description;
    private Date birthday;
    private String country;
    private String city;
    private Date lastLogin;
}
