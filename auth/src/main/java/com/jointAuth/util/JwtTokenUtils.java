package com.jointAuth.util;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.lang.String;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private final ProfileRepository profileRepository;

    public JwtTokenUtils(@Autowired ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public String generateToken(User user) {
        String email = user.getEmail();
        Long userId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        Optional<Profile> curProfile = profileRepository.findByUserId(userId);
        Long profileId = curProfile.map(Profile::getId).orElse(null);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userId", userId);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);
        claims.put("profileId", profileId);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getFullName(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getCurrentUserId(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = getAllClaimsFromToken(token);

            return claims.get("userId", Long.class);
        } catch (MalformedJwtException e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }

    public Long getCurrentProfileId(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = getAllClaimsFromToken(token);

            return claims.get("profileId", Long.class);
        } catch (MalformedJwtException e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }
}
