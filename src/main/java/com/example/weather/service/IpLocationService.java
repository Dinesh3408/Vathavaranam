package com.example.weather.service;

import com.example.weather.dto.IpApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IpLocationService {

    private final RestTemplate restTemplate;
    private static final String IP_API_URL = "http://ip-api.com/json/%s";

    public IpLocationService() {
        this.restTemplate = new RestTemplate();
    }

    public IpApiResponse getLocation(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)
                || "0:0:0:0:0:0:0:1".equals(ip)) {
            // Fallback for localhost or empty IP
            return null;
        }
        try {
            String url = String.format(IP_API_URL, ip);
            return restTemplate.getForObject(url, IpApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
