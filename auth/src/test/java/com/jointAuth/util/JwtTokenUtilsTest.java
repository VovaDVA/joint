package com.jointAuth.util;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtTokenUtilsTest {
    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private ProfileRepository profileRepository;

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
        // Arrange
        User mockUser = new User();
        mockUser.setEmail("IvanPetrov@gmail.com");
        mockUser.setId(153L);
        mockUser.setFirstName("Ivan");
        mockUser.setLastName("Petrov");

        Profile mockProfile = new Profile();
        mockProfile.setId(153L);
        mockProfile.setUser(mockUser);

        when(profileRepository.findByUserId(153L)).thenReturn(Optional.of(mockProfile));

        // Act
        String token = jwtTokenUtils.generateToken(mockUser);

        // Assert
        Claims claims = Jwts.parser()
                .setSigningKey("yourSecretKey") // Replace with your actual secret key
                .parseClaimsJws(token)
                .getBody();

        assertEquals("IvanPetrov@gmail.com", claims.get("email", String.class));
        assertEquals(153L, claims.get("userId", Long.class));
        assertEquals("Ivan", claims.get("firstName", String.class));
        assertEquals("Petrov", claims.get("lastName", String.class));
        assertEquals(153L, claims.get("profileId", Long.class));

        assertNotNull(claims.get("profileId", Long.class));

        Date issuedDate = claims.getIssuedAt();
        Date expiredDate = claims.getExpiration();
        long tokenLifetimeMillis = expiredDate.getTime() - issuedDate.getTime();

        assertEquals(Duration.ofMinutes(30).toMillis(), tokenLifetimeMillis);
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
        claims.put("userId", 190L);

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

    @Test
    public void testGetCurrentProfileIdSuccessful() {
        Claims claims = Jwts.claims();
        claims.put("profileId", 5L);

        String token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        Long profileId = jwtTokenUtils.getCurrentProfileId(token);

        assertEquals(5L, profileId);
    }

    @Test
    public void testGetCurrentProfileIdNullToken() {
        Long profileId = jwtTokenUtils.getCurrentProfileId(null);
        assertNull(profileId);
    }

    @Test
    public void testGetCurrentProfileIdInvalidToken() {
        String invalidToken = "invalid.token.string";

        Long profileId = jwtTokenUtils.getCurrentProfileId(invalidToken);
        assertNull(profileId);
    }

    @Test
    public void testGetCurrentProfileIdMissingId() {
        Claims claims = Jwts.claims();
        claims.put("username", "Evelina_Matveeva");

        String token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        Long profileId = jwtTokenUtils.getCurrentProfileId(token);
        assertNull(profileId);
    }

    @Test
    public void testGetCurrentProfileIdDifferentCases() {
        Claims claims = Jwts.claims();
        claims.put("profileId", 3L);
        claims.put("email", "different_case@example.com");
        claims.put("someOtherKey", "unexpectedValue");

        String token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();

        Long profileId = jwtTokenUtils.getCurrentProfileId(token);
        assertEquals(3L, profileId);
    }
}

