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

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnalyticsAspect.class);
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
            log.info("AnalyticsAspect: Intercepted call to {}", joinPoint.getSignature().getName());
            // Record general hit and visitor for any weather controller method
            analyticsService.recordHit();

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            String ip = getClientIP(request);
            log.info("AnalyticsAspect: Recording visitor from IP {}", ip);
            analyticsService.recordVisitor(ip);
        } catch (Exception e) {
            log.error("AnalyticsAspect: Error recording general analytics: {}", e.getMessage());
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.weather.controller.WeatherController.getWeatherByCity(..)) && args(city, ..)", argNames = "city")
    public void recordCitySearch(String city) {
        try {
            log.info("AnalyticsAspect: Recording city search for {}", city);
            analyticsService.recordCitySearch(city);
        } catch (Exception e) {
            log.error("AnalyticsAspect: Error recording city search for {}: {}", city, e.getMessage());
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
