package entity;


import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

 @Entity
 @Table(name = "profiles")
public class Profile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "banner")
  private String banner;

  @Column(name = "birthday")
  @Temporal(TemporalType.DATE)
  private Date birthday;

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
