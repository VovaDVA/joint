package com.jointAuth.model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "profiles")
public class Profile {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "banner")
    private String banner;

    @Column(name = "description")
    private String description;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "phone")
    private String phone;

    @Column(name = "last_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEdited;
}