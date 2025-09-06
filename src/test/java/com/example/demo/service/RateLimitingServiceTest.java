package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitingServiceTest {
    
    private RateLimitingService rateLimitingService;
    
    @BeforeEach
    void setUp() {
        rateLimitingService = new RateLimitingService();
    }
    
    @Test
    @DisplayName("Should allow requests within rate limit")
    void shouldAllowRequestsWithinRateLimit() {
        String testIp = "192.168.1.1";
        
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.isAllowed(testIp), 
                "Request " + (i + 1) + " should be allowed");
        }
    }
    
    @Test
    @DisplayName("Should block requests exceeding rate limit")
    void shouldBlockRequestsExceedingRateLimit() {
        String testIp = "192.168.1.2";
        
        for (int i = 0; i < 5; i++) {
            rateLimitingService.isAllowed(testIp);
        }
        
        
        assertFalse(rateLimitingService.isAllowed(testIp), 
            "6th request should be blocked");
    }
    
    @Test
    @DisplayName("Should handle different IPs independently")
    void shouldHandleDifferentIpsIndependently() {
        String ip1 = "192.168.1.3";
        String ip2 = "192.168.1.4";
        
        
        for (int i = 0; i < 5; i++) {
            rateLimitingService.isAllowed(ip1);
        }
        
        
        assertFalse(rateLimitingService.isAllowed(ip1));
        
       
        assertTrue(rateLimitingService.isAllowed(ip2));
    }
    
    @Test
    @DisplayName("Should return correct available tokens count")
    void shouldReturnCorrectAvailableTokensCount() {
        String testIp = "192.168.1.5";
        
        
        assertEquals(5, rateLimitingService.getAvailableTokens(testIp));
        
       
        rateLimitingService.isAllowed(testIp);
        rateLimitingService.isAllowed(testIp);
        
        assertEquals(3, rateLimitingService.getAvailableTokens(testIp));
    }
    
    @Test
    @DisplayName("Should return correct wait time when rate limited")
    void shouldReturnCorrectWaitTimeWhenRateLimited() {
        String testIp = "192.168.1.6";
        
      
        for (int i = 0; i < 5; i++) {
            rateLimitingService.isAllowed(testIp);
        }
        
        
        long waitTime = rateLimitingService.getSecondsToWaitForRefill(testIp);
        assertEquals(60, waitTime);
    }
}