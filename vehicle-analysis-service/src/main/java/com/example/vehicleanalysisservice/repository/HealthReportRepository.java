package com.example.vehicleanalysisservice.repository;

import com.example.vehicleanalysisservice.entity.HealthReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
    Optional<HealthReport> findFirstByVinOrderByIdDesc(String vin);
}
