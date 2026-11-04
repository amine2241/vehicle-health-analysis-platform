package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuelRuleTest {
    private final FuelRule rule = new FuelRule();

    @Test
    void shouldReturnNoPenaltyForNormalFuelLevel() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
    }

    @Test
    void shouldApplyTwoPenaltyForLowFuel() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 8,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(2, result.penalty());
    }

    @Test
    void shouldApplyFivePenaltyForVeryLowFuel() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 3,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(5, result.penalty());
    }
}
