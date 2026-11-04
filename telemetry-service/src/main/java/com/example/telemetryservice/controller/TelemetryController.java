package com.example.telemetryservice.controller;

import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.service.TelemetryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryController {
    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveTelemetry(@RequestBody Telemetry telemetry) {
        telemetryService.processTelemetry(telemetry);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
