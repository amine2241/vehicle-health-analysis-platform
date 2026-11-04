package com.example.telemetryservice.repository;

import com.example.telemetryservice.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryRepository extends JpaRepository<Telemetry,Long> {
}
