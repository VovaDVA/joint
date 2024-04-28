package com.jointAuth.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "two_factor_auth_verification_codes")
public class TwoFactorAuthVerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "code", length = 6)
    private String code;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
}
