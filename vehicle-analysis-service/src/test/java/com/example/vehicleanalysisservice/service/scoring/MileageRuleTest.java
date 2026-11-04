package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MileageRuleTest {
    private final MileageRule rule = new MileageRule();

    @Test
    void shouldReturnNoPenaltyForNormalMileage() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
        assertTrue(result.alerts().isEmpty());
    }

    @Test
    void shouldApplyFivePenaltyAbove100000Km() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 120000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(5, result.penalty());
        assertEquals(List.of("Elevated mileage"), result.alerts());
    }

    @Test
    void shouldApplyTenPenaltyAbove150000Km() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 170000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(10, result.penalty());
        assertEquals(List.of("High mileage"), result.alerts());
    }

    @Test
    void shouldApplyFifteenPenaltyAbove200000Km() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 220000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(15, result.penalty());
        assertEquals(List.of("Very high mileage"), result.alerts());
    }
}
