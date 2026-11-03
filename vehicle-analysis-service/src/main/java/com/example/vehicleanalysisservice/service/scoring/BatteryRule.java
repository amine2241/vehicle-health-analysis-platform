package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.RuleResult;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.ScoringRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BatteryRule implements ScoringRule {

        @Override
        public RuleResult evaluate(VehicleData vehicle) {

            double voltage = vehicle.batteryVoltage();

            if (voltage < 11.5) {
                return new RuleResult(20, List.of("Critical battery voltage"));
            }

            if (voltage < 12.0) {
                return new RuleResult(10, List.of("Low battery voltage"));
            }

            return new RuleResult(0, List.of());
        }
    }

