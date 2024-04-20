package com.jointAuth.util;

import com.jointAuth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class JwtTokenUtilsTest {
    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private User mockUser;

    private final Duration jwtLifetime = Duration.ofMinutes(30);

    private final String secret = "yourSecretKey";

    @BeforeEach
    public void setUp() {
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


    @Test
    public void testGetFullNameSuccessful() {
        Claims claims = Jwts.claims().setSubject("Vitally Novikov");

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        String fullName = jwtTokenUtils.getFullName(token);

        assertEquals("Vitally Novikov", fullName);
    }

    @Test
    public void testGetFullNameNullToken() {
        String fullName = jwtTokenUtils.getFullName(null);

        assertNull(fullName);
    }

    @Test
    public void testGetFullNameInvalidToken() {
        String invalidToken = "invalid.token.string";

        String fullName = null;
        try {
            fullName = jwtTokenUtils.getFullName(invalidToken);
            fail("Expected MalformedJwtException to be thrown");
        } catch (MalformedJwtException e) {
            assertNull(fullName);
        }
    }

    @Test
    public void testGetCurrentUserIdSuccessful() {
        Claims claims = Jwts.claims();
        claims.put("id", 190L);

        String token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        Long userId = jwtTokenUtils.getCurrentUserId(token);

        assertEquals(190L, userId);
    }

    @Test
    public void testGetCurrentUserIdNullToken() {
        Long userId = jwtTokenUtils.getCurrentUserId(null);

        assertNull(userId);
    }

    @Test
    public void testGetCurrentUserIdInvalidToken() {
        String invalidToken = "invalid.token.string";

        Long userId = jwtTokenUtils.getCurrentUserId(invalidToken);

        assertNull(userId);
    }

    @Test
    public void testGetCurrentUserIdMissingId() {
        Claims claims = Jwts.claims();
        claims.put("username", "Evelina_Matveeva");

        String token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        Long userId = jwtTokenUtils.getCurrentUserId(token);

        assertNull(userId);
    }
}

