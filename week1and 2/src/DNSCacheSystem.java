import java.util.*;

/**
 * Problem 3: DNS Cache with TTL
 *
 * Simulates a DNS caching system that stores domain -> IP mappings
 * with TTL expiration and LRU eviction.
 *
 * @author Karthick
 * @version 1.0
 */

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, long ttlSeconds) {

        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {

        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheSystem {

    private HashMap<String, DNSEntry> cache = new HashMap<>();

    private int maxSize = 5;

    private int cacheHits = 0;
    private int cacheMisses = 0;

    // Simulated upstream DNS query
    private String queryUpstreamDNS(String domain) {

        return "172.217." + new Random().nextInt(200) + "." + new Random().nextInt(200);
    }

    // Resolve domain
    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {

            cacheHits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        if (cache.size() >= maxSize) {

            // simple eviction
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        cache.put(domain, new DNSEntry(domain, ip, 5));

        return "Cache MISS → " + ip;
    }

    // Cache statistics
    public void getCacheStats() {

        int total = cacheHits + cacheMisses;

        double hitRate = total == 0 ? 0 : (cacheHits * 100.0 / total);

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCacheSystem dns = new DNSCacheSystem();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}