package com.example.vehicleanalysisservice.service;

import com.example.vehicleanalysisservice.client.RecallApiClient;
import com.example.vehicleanalysisservice.entity.*;
import com.example.vehicleanalysisservice.repository.HealthReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleAnalysisServiceTest {

    @Mock
    private RecallApiClient recallApiClient;

    @Mock
    private HealthReportRepository healthReportRepository;


    private VehicleAnalysisService vehicleAnalysisService;

    @BeforeEach
    void setUp() {
        vehicleAnalysisService = new VehicleAnalysisService(
                recallApiClient,
                new HealthScoreCalculator(),
                healthReportRepository
        );
    }

    @Test
    void shouldCalculateAndSaveHealthReport() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 120000,
                new TirePressures(1.6, 2.2, 2.1, 2.2),
                11.8, 105, 8,
                List.of("P0301"), List.of()
        );

        when(recallApiClient.getRecalls(vehicle.vin())).thenReturn(List.of());

        HealthReportResponse result = vehicleAnalysisService.analyze(vehicle);

        assertEquals(60, result.score());
        assertEquals(HealthStatus.ATTENTION, result.status());

        ArgumentCaptor<HealthReport> captor =
                ArgumentCaptor.forClass(HealthReport.class);

        verify(healthReportRepository).save(captor.capture());

        HealthReport savedReport = captor.getValue();

        assertEquals(vehicle.vin(), savedReport.getVin());
        assertEquals(60, savedReport.getScore());
        assertEquals(HealthStatus.ATTENTION, savedReport.getStatus());
        assertEquals(6, savedReport.getAlerts().size());
    }

    @Test
    void shouldIncludeRecallPenaltiesInHealthReport() {
        VehicleData vehicle = new VehicleData(
                "VF123456789", 50000,
                new TirePressures(2.2, 2.2, 2.2, 2.2),
                12.5, 90, 50,
                List.of(), List.of()
        );

        when(recallApiClient.getRecalls(vehicle.vin())).thenReturn(
                List.of(new Recall("R1", "Active recall", true))
        );

        HealthReportResponse result = vehicleAnalysisService.analyze(vehicle);

        assertEquals(90, result.score());
        assertEquals(HealthStatus.HEALTHY, result.status());
        assertEquals(1, result.alerts().size());

        verify(healthReportRepository).save(any(HealthReport.class));
    }

    @Test
    void shouldReturnEmptyAlertsWhenNoReportExists() {
        when(healthReportRepository.findByVinOrderByIdDesc("VF123456789"))
                .thenReturn(List.of());

        List<String> alerts =
                vehicleAnalysisService.getAlerts("VF123456789");

        assertTrue(alerts.isEmpty());
    }

    @Test
    void shouldReturnLatestReportAlerts() {
        HealthReport report = new HealthReport();
        report.setVin("VF123456789");
        report.setAlerts(List.of("Low tire pressure"));

        when(healthReportRepository.findByVinOrderByIdDesc("VF123456789"))
                .thenReturn(List.of(report));

        List<String> alerts =
                vehicleAnalysisService.getAlerts("VF123456789");

        assertEquals(List.of("Low tire pressure"), alerts);
    }

    @Test
    void shouldReturnNullWhenLatestReportDoesNotExist() {
        when(healthReportRepository.findFirstByVinOrderByIdDesc("VF123456789"))
                .thenReturn(java.util.Optional.empty());

        HealthReportResponse result =
                vehicleAnalysisService.getLatestReport("VF123456789");

        assertNull(result);
    }
}