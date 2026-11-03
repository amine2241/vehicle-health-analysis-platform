package com.example.vehicleanalysisservice.service;

import com.example.vehicleanalysisservice.entity.HealthReport;
import com.example.vehicleanalysisservice.entity.HealthStatus;
import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.scoring.*;

import java.util.ArrayList;
import java.util.List;

public class HealthScoreCalculator {
    private final List<ScoringRule> rules;

    public HealthScoreCalculator() {
        this.rules = List.of(
                new MileageRule(),
                new TirePressureRule(),
                new BatteryRule(),
                new EngineTemperatureRule(),
                new FuelRule(),
                new ErrorCodeRule(),
                new RecallRule()
        );
    }

    public HealthReport calculate(VehicleData vehicle) {

        int score = 100;
        List<String> alerts = new ArrayList<>();

        for (ScoringRule rule : rules) {

            RuleResult result = rule.evaluate(vehicle);

            score -= result.penalty();
            alerts.addAll(result.alerts());
        }

        score = Math.max(0, score);

        HealthStatus status;

        if (score >= 80) {
            status = HealthStatus.HEALTHY;
        } else if (score >= 50) {
            status = HealthStatus.ATTENTION;
        } else {
            status = HealthStatus.CRITICAL;
        }

        return new HealthReport(
                vehicle.vin(),
                score,
                status,
                alerts
        );
    }
}
