package com.example.weather.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy
public class RedisConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        log.info("--- Redis Connectivity Debug ---");
        log.info("Redis URL from Properties: {}", redisUrl.replaceAll(":.*@", ":****@"));

        if (connectionFactory instanceof org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory) {
            var lcf = (org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory) connectionFactory;
            log.info("Host: {}, Port: {}, SSL: {}", lcf.getHostName(), lcf.getPort(), lcf.isUseSsl());
        }
        log.info("--------------------------------");

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    public org.springframework.boot.ApplicationRunner connectionTest(RedisConnectionFactory factory) {
        return args -> {
            try {
                log.info("Testing Redis connection...");
                String response = factory.getConnection().ping();
                log.info("Redis Ping Response: {}", response);
                log.info("✅ Successfully connected to Redis!");
            } catch (Exception e) {
                log.error("❌ FAILED to connect to Redis: {}", e.getMessage());
                log.error("Tip: Check if REDIS_URL environment variable is correctly set in Render Dashboard.");
            }
        };
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.warn("Cache 'get' failed for key '{}' in cache '{}'. Treating as a miss. Error: {}",
                        key, cache.getName(), exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.error("Cache 'put' failed for key '{}' in cache '{}'. Error: {}",
                        key, cache.getName(), exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.error("Cache 'evict' failed for key '{}' in cache '{}'. Error: {}",
                        key, cache.getName(), exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.error("Cache 'clear' failed for cache '{}'. Error: {}",
                        cache.getName(), exception.getMessage());
            }
        };
    }
}
