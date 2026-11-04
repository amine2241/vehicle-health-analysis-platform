package com.example.vehicleanalysisservice.controller;

import com.example.vehicleanalysisservice.dto.HealthReportResponse;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.service.VehicleAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VehicleAnalysisController {
    private final VehicleAnalysisService vehicleAnalysisService;

    public VehicleAnalysisController(VehicleAnalysisService vehicleAnalysisService) {
        this.vehicleAnalysisService = vehicleAnalysisService;
    }
    @PostMapping("/analysis")
    public ResponseEntity<HealthReportResponse> analyze(@RequestBody VehicleData vehicle) {
        return ResponseEntity.ok(vehicleAnalysisService.analyze(vehicle));
    }
    @GetMapping("/vehicles/{vin}/health-report")
    public ResponseEntity<HealthReportResponse> getHealthReport(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleAnalysisService.getLatestReport(vin));
    }
    @GetMapping("/vehicles/{vin}/alerts")
    public ResponseEntity<List<String>> getAlerts(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleAnalysisService.getAlerts(vin));
    }

}
