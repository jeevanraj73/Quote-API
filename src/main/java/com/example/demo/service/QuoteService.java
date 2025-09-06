package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class QuoteService {
    
    private static final List<String> INSPIRATIONAL_QUOTES = Arrays.asList(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "It is during our darkest moments that we must focus to see the light. - Aristotle",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
        "It is never too late to be what you might have been. - George Eliot",
        "The way to get started is to quit talking and begin doing. - Walt Disney",
        "Don't let yesterday take up too much of today. - Will Rogers",
        "You learn more from failure than from success. Don't let it stop you. Failure builds character. - Unknown",
        "If you are working on something that you really care about, you don't have to be pushed. The vision pulls you. - Steve Jobs",
        "People who are crazy enough to think they can change the world, are the ones who do. - Rob Siltanen",
        "We don't make mistakes, just happy little accidents. - Bob Ross",
        "Failure is the condiment that gives success its flavor. - Truman Capote",
        "The only impossible journey is the one you never begin. - Tony Robbins",
        "In the midst of winter, I found there was, within me, an invincible summer. - Albert Camus",
        "What lies behind us and what lies before us are tiny matters compared to what lies within us. - Ralph Waldo Emerson",
        "Success is walking from failure to failure with no loss of enthusiasm. - Winston Churchill",
        "Try not to become a person of success, but rather try to become a person of value. - Albert Einstein",
        "Great things never come from comfort zones. - Anonymous",
        "Dream big and dare to fail. - Norman Vaughan"
    );
    
    private final Random random = new Random(); 
 
    public String getRandomQuote() {
        int index = random.nextInt(INSPIRATIONAL_QUOTES.size());
        return INSPIRATIONAL_QUOTES.get(index);
    }
    
    public int getQuoteCount() {
        return INSPIRATIONAL_QUOTES.size();
    }
}