package com.sprinttracker.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-only-secret-key-for-junit-tests-devpulse-32bytes-minimum", 60_000);
    }

    @Test
    void generatesTokenWithExtractableClaims() {
        String token = jwtService.generateToken(42L, "user@example.com");

        assertTrue(jwtService.isTokenValid(token));
        assertEquals("user@example.com", jwtService.extractEmail(token));
        assertEquals(42L, jwtService.extractUserId(token));
    }

    @Test
    void rejectsExpiredOrMalformedToken() {
        assertFalse(jwtService.isTokenValid("not-a-real-token"));
    }

    @Test
    void expiredTokenIsInvalid() {
        JwtService shortLived = new JwtService("test-only-secret-key-for-junit-tests-devpulse-32bytes-minimum", -1000);
        String token = shortLived.generateToken(1L, "expired@example.com");
        assertFalse(shortLived.isTokenValid(token));
    }
}
