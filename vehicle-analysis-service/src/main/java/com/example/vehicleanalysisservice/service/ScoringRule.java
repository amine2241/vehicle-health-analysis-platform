package com.example.vehicleanalysisservice.service;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;

public interface ScoringRule {
    RuleResult evaluate(VehicleData data);
}
