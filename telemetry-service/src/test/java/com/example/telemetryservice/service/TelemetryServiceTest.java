package com.example.telemetryservice.service;

import com.example.telemetryservice.client.VehicleAnalysisClient;
import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.repository.TelemetryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelemetryServiceTest {

    @Mock
    private TelemetryRepository telemetryRepository;

    @Mock
    private VehicleAnalysisClient vehicleAnalysisClient;

    private TelemetryService telemetryService;

    @BeforeEach
    void setUp() {
        telemetryService = new TelemetryService(
                telemetryRepository,
                vehicleAnalysisClient
        );
    }

    @Test
    void shouldSaveTelemetryAndSendItForAnalysis() {

        Telemetry telemetry = new Telemetry();
        telemetry.setVin("VF123456789");
        telemetry.setMileageKm(120000);

        telemetryService.processTelemetry(telemetry);

        verify(telemetryRepository).save(telemetry);
        verify(vehicleAnalysisClient).sendForAnalysis(telemetry);
    }
}