package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FuelRule implements ScoringRule {
    @Override
    public RuleResult evaluate(VehicleData vehicle) {

        int fuelLevel = vehicle.fuelLevelPercent();

        if (fuelLevel < 5) {
            return new RuleResult(5, List.of("Critical fuel level"));
        }

        if (fuelLevel < 10) {
            return new RuleResult(2, List.of("Low fuel level"));
        }

        return new RuleResult(0, List.of());
    }
}
