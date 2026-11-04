package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EngineTemperatureRuleTest {
    private final EngineTemperatureRule rule = new EngineTemperatureRule();

    @Test
    void shouldReturnNoPenaltyForNormalTemperature() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
    }

    @Test
    void shouldApplyTenPenaltyForHighTemperature() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 105, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(10, result.penalty());
    }

    @Test
    void shouldApplyTwentyPenaltyForVeryHighTemperature() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 115, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(20, result.penalty());
    }
}
