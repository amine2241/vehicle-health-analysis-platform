package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorCodeRuleTest {
    private final ErrorCodeRule rule = new ErrorCodeRule();

    @Test
    void shouldReturnNoPenaltyWithoutErrorCodes() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
    }

    @Test
    void shouldApplyFivePenaltyPerErrorCode() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of("P0301", "P0302"), List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(10, result.penalty());
    }

    @Test
    void shouldCapPenaltyAtTwentyFive() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of("P1", "P2", "P3", "P4", "P5", "P6"),
                List.of()
        );

        var result = rule.evaluate(vehicle);

        assertEquals(25, result.penalty());
    }
}
