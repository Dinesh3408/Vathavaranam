package com.example.weather.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_ips")
public class VisitorIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ipAddress;

    private LocalDateTime firstVisit;

    public VisitorIP() {
    }

    public VisitorIP(String ipAddress) {
        this.ipAddress = ipAddress;
        this.firstVisit = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getFirstVisit() {
        return firstVisit;
    }

    public void setFirstVisit(LocalDateTime firstVisit) {
        this.firstVisit = firstVisit;
    }
}
