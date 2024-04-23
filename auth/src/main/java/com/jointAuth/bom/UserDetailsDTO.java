package com.jointAuth.bom;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailsDTO {
    private String firstName;
    private String lastName;

    private String description;
    private String birthday;
    private String country;
    private String city;
    private Date lastLogin;
}
