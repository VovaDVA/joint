package com.jointAuth.util;

import com.jointAuth.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.lang.String;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public String generateToken(User user) {
        String email = user.getEmail();
        Long userId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", userId);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);

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

            return claims.get("id", Long.class);
        } catch (MalformedJwtException e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }
}
