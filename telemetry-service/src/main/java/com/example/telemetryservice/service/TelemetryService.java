package com.example.telemetryservice.service;

import com.example.telemetryservice.client.VehicleAnalysisClient;
import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.repository.TelemetryRepository;
import org.springframework.stereotype.Service;

@Service
public class TelemetryService {
    private final TelemetryRepository telemetryRepository;
    private final VehicleAnalysisClient vehicleAnalysisClient;

    public TelemetryService(
            TelemetryRepository telemetryRepository,
            VehicleAnalysisClient vehicleAnalysisClient) {
        this.telemetryRepository = telemetryRepository;
        this.vehicleAnalysisClient = vehicleAnalysisClient;
    }

    public void processTelemetry(Telemetry telemetry) {
        telemetryRepository.save(telemetry);
        vehicleAnalysisClient.sendForAnalysis(telemetry);
    }
}
