package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.TirePressures;
import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TirePressureRuleTest {
    private final TirePressureRule rule = new TirePressureRule();

    @Test
    void shouldReturnNoPenaltyForNormalPressure() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000,
                new TirePressures(2.2, 2.2, 2.2, 2.2),
                12.5, 90, 50, List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
        assertTrue(result.alerts().isEmpty());
    }

    @Test
    void shouldApplyEightPenaltyForLowPressure() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000,
                new TirePressures(1.7, 2.2, 2.2, 2.2),
                12.5, 90, 50, List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(8, result.penalty());
        assertFalse(result.alerts().isEmpty());
    }

    @Test
    void shouldApplyFifteenPenaltyForVeryLowPressure() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000,
                new TirePressures(1.4, 2.2, 2.2, 2.2),
                12.5, 90, 50, List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(15, result.penalty());
        assertFalse(result.alerts().isEmpty());
    }
}
