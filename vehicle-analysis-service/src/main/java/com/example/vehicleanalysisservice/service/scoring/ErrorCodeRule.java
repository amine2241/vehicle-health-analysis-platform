package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ErrorCodeRule implements ScoringRule {

    private static final int PENALTY_PER_CODE = 5;
    private static final int MAX_PENALTY = 25;

    @Override
    public RuleResult evaluate(VehicleData vehicle) {

        List<String> errorCodes = vehicle.errorCodes();

        if (errorCodes.isEmpty()) {
            return new RuleResult(0, List.of());
        }

        int penalty = Math.min(
                errorCodes.size() * PENALTY_PER_CODE,
                MAX_PENALTY
        );

        return new RuleResult(
                penalty,
                List.of(errorCodes.size() + " vehicle error code(s) detected")
        );
    }
}
