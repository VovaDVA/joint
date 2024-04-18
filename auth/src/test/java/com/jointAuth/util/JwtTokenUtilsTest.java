package com.jointAuth.util;

import com.jointAuth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtTokenUtilsTest {
    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private User mockUser;

    private final Duration jwtLifetime = Duration.ofMinutes(30);

    private final String secret = "yourSecretKey";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenUtils, "jwtLifetime", jwtLifetime);
        ReflectionTestUtils.setField(jwtTokenUtils, "secret", secret);
    }

    @Test
    public void testGenerateTokenSuccessful() {
        when(mockUser.getEmail()).thenReturn("IvanPetrov@gmail.com");
        when(mockUser.getId()).thenReturn(153L);
        when(mockUser.getFirstName()).thenReturn("Ivan");
        when(mockUser.getLastName()).thenReturn("Petrov");

        String token = jwtTokenUtils.generateToken(mockUser);

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        assertEquals("IvanPetrov@gmail.com", claims.get("email", String.class));
        assertEquals(153L, claims.get("id", Long.class));
        assertEquals("Ivan", claims.get("firstName", String.class));
        assertEquals("Petrov", claims.get("lastName", String.class));

        Date issuedDate = claims.getIssuedAt();
        Date expiredDate = claims.getExpiration();
        long tokenLifetimeMillis = expiredDate.getTime() - issuedDate.getTime();
        assertEquals(jwtLifetime.toMillis(), tokenLifetimeMillis);
    }

    @Test
    public void testGenerateTokenJwtParserException() {
        String validToken = jwtTokenUtils.generateToken(mockUser);

        JwtException exception = assertThrows(JwtException.class, () -> {
            jwtTokenUtils.getFullName(validToken + "invalid");
        });

        assertNotNull(exception);
    }

}

