package com.example.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AnalyticsService {
    private final StringRedisTemplate redisTemplate;
    // Keys for Redis
    private static final String KEY_TOTAL_HITS = "analytics:total_hits";
    private static final String KEY_VISITORS = "analytics:visitors";
    private static final String KEY_TOP_CITIES = "analytics:top_cities";

    public AnalyticsService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final LocalDateTime startTime = LocalDateTime.now();

    public long recordHit() {
        return redisTemplate.opsForValue().increment(KEY_TOTAL_HITS);
    }

    public void recordVisitor(String ipAddress) {
        redisTemplate.opsForSet().add(KEY_VISITORS, ipAddress);
    }

    public void recordCitySearch(String city) {
        redisTemplate.opsForZSet().incrementScore(KEY_TOP_CITIES, city.toLowerCase(), 1);
    }

    public long getTotalHits() {
        String hits = redisTemplate.opsForValue().get(KEY_TOTAL_HITS);
        return hits != null ? Long.parseLong(hits) : 0;
    }

    public long getUniqueVisitors() {
        return redisTemplate.opsForSet().size(KEY_VISITORS);
    }

    public Map<String, Long> getTopCities() {
        // Get top 10 cities
        Set<String> cities = redisTemplate.opsForZSet().reverseRange(KEY_TOP_CITIES, 0, 9);
        Map<String, Long> result = new HashMap<>();
        if (cities != null) {
            for (String city : cities) {
                Double score = redisTemplate.opsForZSet().score(KEY_TOP_CITIES, city);
                result.put(city, score != null ? score.longValue() : 0);
            }
        }
        return result;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
