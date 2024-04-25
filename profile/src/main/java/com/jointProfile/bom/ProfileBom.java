package com.jointProfile.bom;

import lombok.Data;

@Data
public class ProfileBom {
    private Long profileId;
    private Long userId;
    private String avatar;
    private String banner;
    private String description;
    private String birthday;
    private String country;
    private String city;
    private String phone;
    private String lastEdited;
}