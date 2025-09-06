package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response for rate limiting")
public class ErrorResponse {
    
    @JsonProperty("error")
    @Schema(description = "Error message", example = "Rate limit exceeded. Try again in 60 seconds.")
    private String error;
    
    public ErrorResponse() {}
    
    public ErrorResponse(String error) {
        this.error = error;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}