package com.example.vehicleanalysisservice.entity;

import java.util.List;

public record RuleResult(
        int penalty,
        List<String> alerts
) {
    public RuleResult {
        alerts = alerts == null ? List.of() : List.copyOf(alerts);
    }
}