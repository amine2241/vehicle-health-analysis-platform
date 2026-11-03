package com.example.vehicleanalysisservice.entity;

import java.util.List;

public record HealthReportResponse(
        String vin,
        int score,
        HealthStatus status,
        List<String> alerts
) {
    public HealthReportResponse {
        alerts = alerts == null ? List.of() : List.copyOf(alerts);
    }
}