package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

@Service
public class MileageRule implements ScoringRule {

    @Override
    public RuleResult evaluate(VehicleData vehicle) {

        int mileage = vehicle.mileageKm();

        if (mileage > 200_000) {
            return new RuleResult(15, java.util.List.of("Very high mileage"));
        }

        if (mileage > 150_000) {
            return new RuleResult(10, java.util.List.of("High mileage"));
        }

        if (mileage > 100_000) {
            return new RuleResult(5, java.util.List.of("Elevated mileage"));
        }

        return new RuleResult(0, java.util.List.of());
    }
}
