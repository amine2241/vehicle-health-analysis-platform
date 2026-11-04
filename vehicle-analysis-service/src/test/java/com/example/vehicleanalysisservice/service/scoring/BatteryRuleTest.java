package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BatteryRuleTest {
    private final BatteryRule rule = new BatteryRule();

    @Test
    void shouldReturnNoPenaltyForNormalVoltage() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
        assertTrue(result.alerts().isEmpty());
    }

    @Test
    void shouldApplyTenPenaltyForLowVoltage() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 11.8, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(10, result.penalty());
    }

    @Test
    void shouldApplyTwentyPenaltyForVeryLowVoltage() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 11.0, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(20, result.penalty());
    }
}
