package com.example.vehicleanalysisservice.service.scoring;

import com.example.vehicleanalysisservice.entity.Recall;
import com.example.vehicleanalysisservice.entity.VehicleData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecallRuleTest {
    private final RecallRule rule = new RecallRule();

    @Test
    void shouldReturnNoPenaltyWithoutActiveRecalls() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(),
                List.of(new Recall("R1", "Test recall", false))
        );

        var result = rule.evaluate(vehicle);

        assertEquals(0, result.penalty());
    }

    @Test
    void shouldApplyTenPenaltyPerActiveRecall() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(),
                List.of(
                        new Recall("R1", "Recall one", true),
                        new Recall("R2", "Recall two", true)
                )
        );

        var result = rule.evaluate(vehicle);

        assertEquals(20, result.penalty());
    }

    @Test
    void shouldCapPenaltyAtThirty() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000, null, 12.5, 90, 50,
                List.of(),
                List.of(
                        new Recall("R1", "Recall one", true),
                        new Recall("R2", "Recall two", true),
                        new Recall("R3", "Recall three", true),
                        new Recall("R4", "Recall four", true)
                )
        );

        var result = rule.evaluate(vehicle);

        assertEquals(30, result.penalty());
    }
}
