package com.example.demo.controller;


import com.example.demo.service.QuoteService;
import com.example.demo.service.RateLimitingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuoteController.class)
class QuoteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private QuoteService quoteService;
    
    @MockBean
    private RateLimitingService rateLimitingService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should return quote when rate limit allows")
    void shouldReturnQuoteWhenRateLimitAllows() throws Exception {
        
        String testQuote = "Test quote - Author";
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);
        when(quoteService.getRandomQuote()).thenReturn(testQuote);
        
        
        mockMvc.perform(get("/api/quote"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quote").value(testQuote));
    }
    
    @Test
    @DisplayName("Should return 429 when rate limit exceeded")
    void shouldReturn429WhenRateLimitExceeded() throws Exception {
        
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);
        when(rateLimitingService.getSecondsToWaitForRefill(anyString())).thenReturn(60L);
        
       
        mockMvc.perform(get("/api/quote"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Rate limit exceeded. Try again in 60 seconds."));
    }
    
    @Test
    @DisplayName("Should return health status")
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"));
    }
}