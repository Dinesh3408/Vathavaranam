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
    private final com.example.weather.repository.SearchMetricRepository searchMetricRepository;
    private final com.example.weather.repository.VisitorIPRepository visitorIPRepository;
    private static final Logger log = LoggerFactory.getLogger(RedisAnalyticsService.class);

    // Keys for Redis
    private static final String KEY_TOTAL_HITS = "analytics:total_hits";
    private static final String KEY_VISITORS = "analytics:visitors";
    private static final String KEY_TOP_CITIES = "analytics:top_cities";

    public RedisAnalyticsService(StringRedisTemplate redisTemplate,
            com.example.weather.repository.SearchMetricRepository searchMetricRepository,
            com.example.weather.repository.VisitorIPRepository visitorIPRepository) {
        this.redisTemplate = redisTemplate;
        this.searchMetricRepository = searchMetricRepository;
        this.visitorIPRepository = visitorIPRepository;
    }

    private final LocalDateTime startTime = LocalDateTime.now();

    public long recordHit() {
        try {
            long count = redisTemplate.opsForValue().increment(KEY_TOTAL_HITS);
            log.info("RedisAnalyticsService: Recorded hit. New total hits: {}", count);
            return count;
        } catch (Exception e) {
            log.error("RedisAnalyticsService: Failed to record hit in Redis: {}", e.getMessage());
            return 0;
        }
    }

    public void recordVisitor(String ipAddress) {
        try {
            // Redis (for fast unique count)
            redisTemplate.opsForSet().add(KEY_VISITORS, ipAddress);

            // SQL (for permanent storage)
            if (!visitorIPRepository.existsByIpAddress(ipAddress)) {
                visitorIPRepository.save(new com.example.weather.model.VisitorIP(ipAddress));
                log.info("RedisAnalyticsService: Permanent record saved for new visitor IP: {}", ipAddress);
            }
        } catch (Exception e) {
            log.error("Failed to record visitor: {}", e.getMessage());
        }
    }

    public void recordCitySearch(String city) {
        city = city.toLowerCase().trim();
        try {
            // Redis (for fast top-K)
            redisTemplate.opsForZSet().incrementScore(KEY_TOP_CITIES, city, 1);

            // SQL (for permanent storage)
            com.example.weather.model.SearchMetric metric = searchMetricRepository.findByCityName(city)
                    .orElse(new com.example.weather.model.SearchMetric(city, 0));
            metric.setSearchCount(metric.getSearchCount() + 1);
            searchMetricRepository.save(metric);
            log.info("RedisAnalyticsService: Permanent count updated for city: {}", city);
        } catch (Exception e) {
            log.error("Failed to record city search: {}", e.getMessage());
        }
    }

    public long getTotalHits() {
        try {
            String hits = redisTemplate.opsForValue().get(KEY_TOTAL_HITS);
            return hits != null ? Long.parseLong(hits) : 0;
        } catch (Exception e) {
            log.error("RedisAnalyticsService: Failed to get total hits from Redis: {}", e.getMessage());
            return 0;
        }
    }

    public long getUniqueVisitors() {
        try {
            // Default to Redis for speed
            Long size = redisTemplate.opsForSet().size(KEY_VISITORS);
            if (size == null || size == 0) {
                // Fallback to SQL if Redis is empty or failing
                return visitorIPRepository.count();
            }
            return size;
        } catch (Exception e) {
            log.warn("Redis unique visitor count failed, falling back to SQL: {}", e.getMessage());
            return visitorIPRepository.count();
        }
    }

    public Map<String, Long> getTopCities() {
        try {
            // Get top 3 cities (as requested by user)
            Set<String> cities = redisTemplate.opsForZSet().reverseRange(KEY_TOP_CITIES, 0, 2);
            Map<String, Long> result = new HashMap<>();

            if (cities != null && !cities.isEmpty()) {
                for (String city : cities) {
                    Double score = redisTemplate.opsForZSet().score(KEY_TOP_CITIES, city);
                    result.put(city, score != null ? score.longValue() : 0);
                }
            } else {
                // Fallback to SQL top 3
                searchMetricRepository.findTop3ByOrderBySearchCountDesc()
                        .forEach(m -> result.put(m.getCityName(), m.getSearchCount()));
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to get top cities, falling back to SQL: {}", e.getMessage());
            Map<String, Long> result = new HashMap<>();
            searchMetricRepository.findTop3ByOrderBySearchCountDesc()
                    .forEach(m -> result.put(m.getCityName(), m.getSearchCount()));
            return result;
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
