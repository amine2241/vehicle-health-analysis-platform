package com.example.vehicleanalysisservice.entity;

import java.util.List;

public record HealthReport(
        String vin,
        int score,
        HealthStatus status,
        List<String> alerts
) {
    public HealthReport {
        alerts = alerts == null ? List.of() : List.copyOf(alerts);
    }
}