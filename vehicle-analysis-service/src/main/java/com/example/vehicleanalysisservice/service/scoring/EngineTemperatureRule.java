package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EngineTemperatureRule implements ScoringRule {

    @Override
    public RuleResult evaluate(VehicleData vehicle) {

        int temperature = vehicle.engineTempCelsius();

        if (temperature > 110) {
            return new RuleResult(20, List.of("Critical engine temperature"));
        }

        if (temperature > 100) {
            return new RuleResult(10, List.of("High engine temperature"));
        }

        return new RuleResult(0, List.of());
    }
}