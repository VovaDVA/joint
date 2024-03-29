package com.jointAuth.util;

import com.jointAuth.model.User;
import io.jsonwebtoken.*;
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
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", userId);

        String fullName = user.getFirstName() + " " + user.getLastName();
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(fullName)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getFullName(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            // Токен истек
            return false;
        } catch (MalformedJwtException e) {
            // Неверный формат токена
            return false;
        } catch (UnsupportedJwtException e) {
            // Токен не поддерживается
            return false;
        } catch (SignatureException e) {
            // Ошибка проверки подписи
            return false;
        } catch (Exception e) {
            // Другие ошибки
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
