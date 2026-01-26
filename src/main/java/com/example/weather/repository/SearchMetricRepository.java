package com.example.weather.repository;

import com.example.weather.model.SearchMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SearchMetricRepository extends JpaRepository<SearchMetric, Long> {
    Optional<SearchMetric> findByCityName(String cityName);

    List<SearchMetric> findTop3ByOrderBySearchCountDesc();
}
