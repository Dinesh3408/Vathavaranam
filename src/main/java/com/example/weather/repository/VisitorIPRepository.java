package com.example.weather.repository;

import com.example.weather.model.VisitorIP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorIPRepository extends JpaRepository<VisitorIP, Long> {
    boolean existsByIpAddress(String ipAddress);
}
