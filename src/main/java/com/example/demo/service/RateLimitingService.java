package com.example.demo.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RateLimitingService {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitingService.class);
    
    private final ConcurrentMap<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private final BucketConfiguration bucketConfiguration;
    
    private static final int CAPACITY = 5;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);
    
    public RateLimitingService() {
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.greedy(CAPACITY, REFILL_PERIOD));
        this.bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }
    
    public boolean isAllowed(String ip) {
        Bucket bucket = ipBuckets.computeIfAbsent(ip, k -> {
            logger.info("Creating new bucket for IP: {}", ip);
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(CAPACITY, Refill.greedy(CAPACITY, REFILL_PERIOD)))
                    .build();
        });
        
        boolean allowed = bucket.tryConsume(1);
        logger.debug("Rate limit check for IP {}: {}", ip, allowed ? "ALLOWED" : "BLOCKED");
        return allowed;
    }
    
    public long getSecondsToWaitForRefill(String ip) {
        Bucket bucket = ipBuckets.get(ip);
        if (bucket != null && bucket.getAvailableTokens() == 0) {
            return REFILL_PERIOD.getSeconds();
        }
        return 0;
    }
   
    public long getAvailableTokens(String ip) {
        Bucket bucket = ipBuckets.get(ip);
        return bucket != null ? bucket.getAvailableTokens() : CAPACITY;
    }
}
