package com.example.telemetryservice.client;

import java.util.List;

public record VehicleDataRequest(
        String vin,
        int mileageKm,
        TirePressures tirePressures,
        double batteryVoltage,
        int engineTempCelsius,
        int fuelLevelPercent,
        List<String> errorCodes
) {
    public record TirePressures(
            double frontLeft,
            double frontRight,
            double rearLeft,
            double rearRight
    ) {}
}