package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Quote response containing an inspirational quote")
public class QuoteResponse {
    
    @JsonProperty("quote")
    @Schema(description = "Random inspirational quote", example = "The only way to do great work is to love what you do. - Steve Jobs")
    private String quote;
    
    public QuoteResponse() {}
    
    public QuoteResponse(String quote) {
        this.quote = quote;
    }
    
    public String getQuote() {
        return quote;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
    }
}