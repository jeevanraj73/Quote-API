package com.example.demo.controller;

import com.example.demo.model.ErrorResponse;
import com.example.demo.model.QuoteResponse;
import com.example.demo.service.QuoteService;
import com.example.demo.service.RateLimitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Quote API", description = "API for retrieving random inspirational quotes with rate limiting")
public class QuoteController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);
    
    private final QuoteService quoteService;
    private final RateLimitingService rateLimitingService;
    
    @Autowired
    public QuoteController(QuoteService quoteService, RateLimitingService rateLimitingService) {
        this.quoteService = quoteService;
        this.rateLimitingService = rateLimitingService;
    }
    
    @GetMapping("/quote")
    @Operation(
        summary = "Get a random inspirational quote",
        description = "Returns a random inspirational quote. Rate limited to 5 requests per minute per IP address."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved a random quote",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuoteResponse.class))
        ),
        @ApiResponse(
            responseCode = "429",
            description = "Rate limit exceeded - too many requests",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<?> getRandomQuote(HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);
        
        if (!rateLimitingService.isAllowed(clientIp)) {
            long waitTime = rateLimitingService.getSecondsToWaitForRefill(clientIp);
            String errorMessage = String.format("Rate limit exceeded. Try again in %d seconds.", waitTime);
            
            logger.warn("Rate limit exceeded for IP: {} - Status: 429", clientIp);
            
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse(errorMessage));
        }
        
        String quote = quoteService.getRandomQuote();
        
        logger.info("Quote served to IP: {} - Status: 200", clientIp);
        
        return ResponseEntity.ok(new QuoteResponse(quote));
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Health check endpoint",
        description = "Returns the health status of the API"
    )
    @ApiResponse(
        responseCode = "200",
        description = "API is healthy",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body("{\"status\": \"UP\", \"message\": \"Quote API is running\"}");
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}