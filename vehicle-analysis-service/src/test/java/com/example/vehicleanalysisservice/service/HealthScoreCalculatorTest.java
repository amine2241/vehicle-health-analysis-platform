package com.example.vehicleanalysisservice.service;

import com.example.vehicleanalysisservice.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthScoreCalculatorTest {
    private final HealthScoreCalculator calculator =
            new HealthScoreCalculator();
    @Test
    void shouldReturnPerfectScoreWhenVehicleHasNoIssues() {
        VehicleData vehicle = new VehicleData(
                "VF1TEST1234567890",
                50000,
                new TirePressures(2.2, 2.2, 2.2, 2.2),
                12.6,
                90,
                80,
                List.of(),
                List.of()
        );

        HealthReportResponse report = calculator.calculate(vehicle);

        assertEquals(100, report.score());
        assertEquals(HealthStatus.HEALTHY, report.status());
        assertEquals(0, report.alerts().size());
    }

    @Test
    void shouldApplyPenaltiesAndDetermineHealthStatus() {
        VehicleData vehicle = new VehicleData(
                "VF1TEST1234567890",
                160000,                         // -10
                new TirePressures(1.7, 2.2, 2.2, 2.2), // -8
                11.8,                           // -10
                105,                            // -10
                4,                              // -5
                List.of("P0301", "P0420"),      // -10
                List.of(new Recall("R001", "Recall", true)) // -10
        );

        HealthReportResponse report = calculator.calculate(vehicle);

        assertEquals(37, report.score());
        assertEquals(HealthStatus.CRITICAL, report.status());
        assertEquals(7, report.alerts().size());
    }
}
