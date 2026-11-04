package com.example.telemetryservice.client;

import com.example.telemetryservice.dto.VehicleDataRequest;
import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.mapper.VehicleDataMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class VehicleAnalysisClient {
    private final RestClient restClient;
    private final VehicleDataMapper vehicleDataMapper;

    public VehicleAnalysisClient(
            @Value("${vehicle-analysis.api.base-url:http://localhost:8082}")
            String baseUrl,
            VehicleDataMapper vehicleDataMapper) {

        this.restClient = RestClient.create(baseUrl);
        this.vehicleDataMapper = vehicleDataMapper;
    }
    @Async
    public void sendForAnalysis(Telemetry telemetry) {

        VehicleDataRequest request = vehicleDataMapper.toRequest(telemetry);
        restClient.post()
                .uri("/api/v1/analysis")
                .body(request )
                .retrieve()
                .toBodilessEntity();
    }
}
