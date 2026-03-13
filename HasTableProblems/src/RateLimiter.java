import java.util.*;

/**
 * Problem 6: Distributed Rate Limiter
 * Implements a token bucket algorithm per client.
 *
 * Author: Karthick
 * Version: 1.0
 */

class TokenBucket {

    int tokens;
    int maxTokens;
    long lastRefillTime;
    int refillRate; // tokens per second

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens
    public synchronized void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;
        int newTokens = (int) (elapsed / 1000) * refillRate;
        if (newTokens > 0) {
            tokens = Math.min(tokens + newTokens, maxTokens);
            lastRefillTime = now;
        }
    }

    // Try consuming a token
    public synchronized boolean tryConsume() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    public synchronized int getRemainingTokens() {
        refill();
        return tokens;
    }
}

public class RateLimiter {

    private HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    private int maxTokens = 1000;
    private int refillRate = 1000; // per hour simplified as 1000 per second for demo

    public String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId, new TokenBucket(maxTokens, refillRate));

        TokenBucket bucket = clientBuckets.get(clientId);

        boolean allowed = bucket.tryConsume();

        if (allowed) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry later)";
        }
    }

    public Map<String, Integer> getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);
        if (bucket == null) return Map.of("used", 0, "limit", maxTokens, "remaining", maxTokens);
        return Map.of(
                "used", maxTokens - bucket.getRemainingTokens(),
                "limit", maxTokens,
                "remaining", bucket.getRemainingTokens()
        );
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String clientId = "abc123";

        System.out.println(limiter.checkRateLimit(clientId));
        System.out.println(limiter.checkRateLimit(clientId));
        System.out.println(limiter.getRateLimitStatus(clientId));
    }
}