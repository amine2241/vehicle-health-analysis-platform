package com.example.vehicleanalysisservice.entity;

import java.util.List;

public record Vehicledata(
        String vin,
        int mileageKm,
        TirePressures tirePressures,
        double batteryVoltage,
        int engineTempCelsius,
        int fuelLevelPercent,
        List<String> errorCodes,
        List<Recall> recalls
) {
}