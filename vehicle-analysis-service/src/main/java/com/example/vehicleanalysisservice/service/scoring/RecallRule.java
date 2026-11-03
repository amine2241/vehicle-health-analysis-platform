package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.Recall;
import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RecallRule implements ScoringRule {

    private static final int PENALTY_PER_RECALL = 10;
    private static final int MAX_PENALTY = 30;

    @Override
    public RuleResult evaluate(VehicleData vehicle) {

        int activeRecallCount = 0;

        for (Recall recall : vehicle.recalls()) {
            if (recall.active()) {
                activeRecallCount++;
            }
        }

        int penalty = Math.min(
                activeRecallCount * PENALTY_PER_RECALL,
                MAX_PENALTY
        );

        if (activeRecallCount == 0) {
            return new RuleResult(0, List.of());
        }

        return new RuleResult(
                penalty,
                List.of(activeRecallCount + " active recall campaign(s) detected")
        );
    }
}
