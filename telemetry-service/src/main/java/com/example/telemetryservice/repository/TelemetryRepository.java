package com.example.telemetryservice.repository;

import com.example.telemetryservice.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry,Long> {
}
