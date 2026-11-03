package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.TirePressures;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TirePressureRule implements ScoringRule {
@Override
public RuleResult evaluate(VehicleData vehicle) {

    TirePressures tires = vehicle.tirePressures();

    double lowestPressure = Math.min(
            Math.min(tires.frontLeft(), tires.frontRight()),
            Math.min(tires.rearLeft(), tires.rearRight())
    );

    if (lowestPressure < 1.5) {
        return new RuleResult(15, List.of("Critical tire pressure"));
    }

    if (lowestPressure < 1.8) {
        return new RuleResult(8, List.of("Low tire pressure"));
    }

    return new RuleResult(0, List.of());
}}
