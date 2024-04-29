package com.jointAuth.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_verification_codes")
public class UserVerificationCode {
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

    @Column(name = "request_type", length = 20)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;
}

