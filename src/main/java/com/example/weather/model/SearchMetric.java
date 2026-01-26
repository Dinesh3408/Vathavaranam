package com.example.weather.model;

import jakarta.persistence.*;

@Entity
@Table(name = "search_metrics")
public class SearchMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cityName;

    private long searchCount;

    public SearchMetric() {
    }

    public SearchMetric(String cityName, long searchCount) {
        this.cityName = cityName;
        this.searchCount = searchCount;
    }

    public Long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(long searchCount) {
        this.searchCount = searchCount;
    }
}
