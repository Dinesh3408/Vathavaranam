package com.example.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisAnalyticsService implements AnalyticsService {
    private final StringRedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisAnalyticsService.class);

    // Keys for Redis
    private static final String KEY_TOTAL_HITS = "analytics:total_hits";
    private static final String KEY_VISITORS = "analytics:visitors";
    private static final String KEY_TOP_CITIES = "analytics:top_cities";

    public RedisAnalyticsService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final LocalDateTime startTime = LocalDateTime.now();

    public long recordHit() {
        try {
            return redisTemplate.opsForValue().increment(KEY_TOTAL_HITS);
        } catch (Exception e) {
            log.error("Failed to record hit in Redis: {}", e.getMessage());
            return 0;
        }
    }

    public void recordVisitor(String ipAddress) {
        try {
            redisTemplate.opsForSet().add(KEY_VISITORS, ipAddress);
        } catch (Exception e) {
            log.error("Failed to record visitor in Redis: {}", e.getMessage());
        }
    }

    public void recordCitySearch(String city) {
        try {
            redisTemplate.opsForZSet().incrementScore(KEY_TOP_CITIES, city.toLowerCase(), 1);
        } catch (Exception e) {
            log.error("Failed to record city search in Redis: {}", e.getMessage());
        }
    }

    public long getTotalHits() {
        try {
            String hits = redisTemplate.opsForValue().get(KEY_TOTAL_HITS);
            return hits != null ? Long.parseLong(hits) : 0;
        } catch (Exception e) {
            log.error("Failed to get total hits from Redis: {}", e.getMessage());
            return 0;
        }
    }

    public long getUniqueVisitors() {
        try {
            Long size = redisTemplate.opsForSet().size(KEY_VISITORS);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Failed to get unique visitors from Redis: {}", e.getMessage());
            return 0;
        }
    }

    public Map<String, Long> getTopCities() {
        try {
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
        } catch (Exception e) {
            log.error("Failed to get top cities from Redis: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
