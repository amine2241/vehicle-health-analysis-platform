package com.example.vehicleanalysisservice.repository;

import com.example.vehicleanalysisservice.entity.HealthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
    Optional<HealthReport> findFirstByVinOrderByIdDesc(String vin);

    List<HealthReport> findByVinOrderByIdDesc(String vin);
}
