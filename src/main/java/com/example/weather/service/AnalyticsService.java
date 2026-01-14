package com.example.weather.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class AnalyticsService {
    private final AtomicLong totalHits  = new AtomicLong();
    private final AtomicLong uniqueVisitors = new AtomicLong();
    private final Map<String, Long> citySearchCount = new HashMap<>();
    private final Map<String,LocalDateTime> visitorLastSeen = new HashMap<>();
    private final LocalDateTime startTime = LocalDateTime.now();

    public long recordHit(){
        return totalHits.incrementAndGet();
    }
    public void recordVisitor(String ipAddress){
        if(!visitorLastSeen.containsKey(ipAddress)){
            uniqueVisitors.incrementAndGet();
        }
        visitorLastSeen.put(ipAddress, LocalDateTime.now());
    }
    public  void recordCitySearch(String city){
        citySearchCount.merge(city.toLowerCase(),1L,Long::sum);
    }
    public long getTotalHits() {
        return totalHits.get();
    }
    public long getUniqueVisitors() {
        return uniqueVisitors.get();
    }
    public Map<String, Long> getTopCities() {
        return citySearchCount;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
}
