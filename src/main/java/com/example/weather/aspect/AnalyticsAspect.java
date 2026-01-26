package com.example.weather.aspect;

import com.example.weather.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AnalyticsAspect {

    private final AnalyticsService analyticsService;

    public AnalyticsAspect(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Pointcut("execution(* com.example.weather.controller.WeatherController.*(..))")
    public void weatherControllerMethods() {
    }

    @AfterReturning("weatherControllerMethods()")
    public void recordGeneralAnalytics(JoinPoint joinPoint) {
        try {
            // Record general hit and visitor for any weather controller method
            analyticsService.recordHit();

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            String ip = getClientIP(request);
            analyticsService.recordVisitor(ip);
        } catch (Exception e) {
            // Silently fail to not disrupt the original request flow
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.weather.controller.WeatherController.getWeatherByCity(..)) && args(city, ..)", argNames = "city")
    public void recordCitySearch(String city) {
        try {
            analyticsService.recordCitySearch(city);
        } catch (Exception e) {
            // Silently fail
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
