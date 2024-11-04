package com.sa.notifications.common.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtOutputAdapterTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private JwtOutputAdapter jwtOutputAdapter;

    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";
    private final String VALID_TOKEN = Jwts.builder()
            .setSubject("testuser")
            .claim("role", "USER")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .compact();

    @BeforeEach
    void setUp() {
        jwtOutputAdapter = new JwtOutputAdapter(SECRET, redisTemplate);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValid_ValidToken() {
        // Given
        when(redisTemplate.hasKey("testuser")).thenReturn(true);

        // When
        boolean isValid = jwtOutputAdapter.isValid(VALID_TOKEN);

        // Then
        assertTrue(!isValid);
    }

    @Test
    void testIsValid_ExpiredToken() {
        // Given
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // already expired
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        
        // When
        boolean isValid = jwtOutputAdapter.isValid(expiredToken);

        // Then
        assertFalse(isValid);
    }


}