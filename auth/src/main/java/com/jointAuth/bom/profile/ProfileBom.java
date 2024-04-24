package com.jointAuth.bom.profile;

import lombok.Data;

@Data
public class ProfileBom {
    private Long profileId;
    private Long userId;
    private String description;
    private String birthday;
    private String country;
    private String city;
    private String phone;
    private String lastEdited;
}
