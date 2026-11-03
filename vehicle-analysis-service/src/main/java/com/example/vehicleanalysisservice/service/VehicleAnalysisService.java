package com.example.vehicleanalysisservice.service;

import com.example.vehicleanalysisservice.client.RecallApiClient;
import com.example.vehicleanalysisservice.entity.HealthReportResponse;
import com.example.vehicleanalysisservice.entity.HealthReport;
import com.example.vehicleanalysisservice.entity.Recall;
import com.example.vehicleanalysisservice.entity.VehicleData;
import com.example.vehicleanalysisservice.repository.HealthReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleAnalysisService {
    private final RecallApiClient recallApiClient;
    private final HealthScoreCalculator healthScoreCalculator;
    private final HealthReportRepository healthReportRepository;

    public VehicleAnalysisService(
            RecallApiClient recallApiClient,
            HealthScoreCalculator healthScoreCalculator,
            HealthReportRepository healthReportRepository
    ) {
        this.recallApiClient = recallApiClient;
        this.healthScoreCalculator = healthScoreCalculator;
        this.healthReportRepository = healthReportRepository;
    }

    public HealthReportResponse analyze(VehicleData vehicle) {

        List<Recall> recalls = recallApiClient.getRecalls(vehicle.vin());

        VehicleData enrichedVehicle = new VehicleData(
                vehicle.vin(),
                vehicle.mileageKm(),
                vehicle.tirePressures(),
                vehicle.batteryVoltage(),
                vehicle.engineTempCelsius(),
                vehicle.fuelLevelPercent(),
                vehicle.errorCodes(),
                recalls
        );

        HealthReportResponse healthReportResponse = healthScoreCalculator.calculate(enrichedVehicle);

        HealthReport healthReport = new HealthReport();
        healthReport.setVin(healthReportResponse.vin());
        healthReport.setScore(healthReportResponse.score());
        healthReport.setStatus(healthReportResponse.status());

        healthReportRepository.save(healthReport);

        return healthReportResponse;
    }

    public List<String> getAlerts(String vin) {
        List<HealthReport> reports =
                healthReportRepository.findByVinOrderByIdDesc(vin);

        if (reports.isEmpty()) {
            return List.of();
        }

        return reports.get(0).getAlerts();
    }
    public HealthReportResponse getLatestReport(String vin) {
        HealthReport report = healthReportRepository
                .findFirstByVinOrderByIdDesc(vin)
                .orElse(null);

        if (report == null) {
            return null;
        }

        return new HealthReportResponse(
                report.getVin(),
                report.getScore(),
                report.getStatus(),
                report.getAlerts()
        );
    }
}
